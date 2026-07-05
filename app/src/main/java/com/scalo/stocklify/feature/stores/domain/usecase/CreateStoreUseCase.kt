package com.scalo.stocklify.feature.stores.domain.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.scalo.stocklify.core.data.database.dao.OrganizationDao
import com.scalo.stocklify.core.data.database.dao.OrganizationMemberDao
import com.scalo.stocklify.core.data.database.dao.UserStoreMappingDao
import com.scalo.stocklify.core.data.database.mapper.toEntity
import com.scalo.stocklify.core.model.enums.MembershipRole
import com.scalo.stocklify.core.model.models.Organization
import com.scalo.stocklify.core.model.models.OrganizationMember
import com.scalo.stocklify.core.model.models.Store
import com.scalo.stocklify.core.model.models.UserStoreMapping
import com.scalo.stocklify.core.util.constants.ConstantPreferences
import com.scalo.stocklify.feature.auth.domain.repository.AuthRepository
import com.scalo.stocklify.feature.stores.domain.repository.StoreRepository
import java.util.UUID
import javax.inject.Inject

class CreateStoreUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
    private val organizationDao: OrganizationDao,
    private val organizationMemberDao: OrganizationMemberDao,
    private val userStoreMappingDao: UserStoreMappingDao,
    private val authRepository: AuthRepository,
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val HAS_STORE_KEY = booleanPreferencesKey(ConstantPreferences.HAS_STORE_KEY)
    }

    suspend operator fun invoke(storeName: String, storeTypeCode: String?): String {
        val user = authRepository.getCurrentUser()
            ?: error("Cannot create a store without an authenticated user")
        val now = System.currentTimeMillis()

        val organizationId = organizationMemberDao.getByUserId(user.id)?.organizationId
            ?: createOrganization(storeName, user.id, now)

        val store = Store(
            organizationId = organizationId,
            storeCode = "STR-${UUID.randomUUID().toString().take(8).uppercase()}",
            storeTypeCode = storeTypeCode ?: "OTHER",
            name = storeName,
            createdAt = now,
            createdBy = user.id
        )
        val storeId = storeRepository.createStore(store)

        val isFirstStore = userStoreMappingDao.getCountByUserId(user.id) == 0
        userStoreMappingDao.insert(
            UserStoreMapping(
                userId = user.id,
                storeId = storeId,
                role = MembershipRole.OWNER,
                isDefaultStore = isFirstStore,
                createdAt = now,
                createdBy = user.id
            ).toEntity()
        )

        dataStore.edit { prefs ->
            prefs[HAS_STORE_KEY] = true
        }

        return storeId
    }

    private suspend fun createOrganization(storeName: String, userId: String, now: Long): String {
        val organization = Organization(
            name = "$storeName Organization",
            createdAt = now,
            createdBy = userId
        )
        organizationDao.insert(organization.toEntity())

        organizationMemberDao.insert(
            OrganizationMember(
                organizationId = organization.id,
                userId = userId,
                role = MembershipRole.OWNER,
                createdAt = now,
                createdBy = userId
            ).toEntity()
        )

        return organization.id
    }
}
