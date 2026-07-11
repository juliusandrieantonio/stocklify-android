package com.scalo.stocklify.feature.stores.domain.usecase

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.scalo.stocklify.core.data.database.dao.OrganizationDao
import com.scalo.stocklify.core.data.database.dao.OrganizationMemberDao
import com.scalo.stocklify.core.data.database.dao.UserStoreMappingDao
import com.scalo.stocklify.core.data.database.mapper.toDto
import com.scalo.stocklify.core.data.database.mapper.toEntity
import com.scalo.stocklify.core.data.sync.SyncOutboxWriter
import com.scalo.stocklify.core.model.models.Organization
import com.scalo.stocklify.core.model.models.OrganizationMember
import com.scalo.stocklify.core.model.models.Store
import com.scalo.stocklify.core.model.models.UserStoreMapping
import com.scalo.stocklify.core.util.constants.ConstantPreferences
import com.scalo.stocklify.core.util.constants.Tables
import com.scalo.stocklify.feature.auth.domain.repository.AuthRepository
import com.scalo.stocklify.feature.stores.domain.repository.StoreRepository
import java.util.UUID
import javax.inject.Inject

class CreateStoreUseCase @Inject constructor(
    private val storeRepository: StoreRepository,
    private val organizationMemberDao: OrganizationMemberDao,
    private val userStoreMappingDao: UserStoreMappingDao,
    private val authRepository: AuthRepository,
    private val syncOutboxWriter: SyncOutboxWriter,
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val HAS_STORE_KEY = booleanPreferencesKey(ConstantPreferences.HAS_STORE_KEY)
    }

    suspend operator fun invoke(storeName: String, storeTypeCode: String?): String {
        val user = authRepository.getCurrentUser()
        val now = System.currentTimeMillis()

        val organizationId = user?.let { organizationMemberDao.getByUserId(it.id)?.organizationId }

        val store = Store(
            organizationId = organizationId,
            storeCode = "STR-${UUID.randomUUID().toString().take(8).uppercase()}",
            storeTypeCode = storeTypeCode ?: "OTHER",
            name = storeName,
            createdAt = now,
            createdBy = user?.id
        )
        val storeId = storeRepository.createStore(store)

        if (user != null) {
            val isFirstStore = userStoreMappingDao.getCountByUserId(user.id) == 0
            val userStoreMapping = UserStoreMapping(
                userId = user.id,
                storeId = storeId,
                isDefaultStore = isFirstStore,
                createdAt = now,
                createdBy = user.id
            )
            userStoreMappingDao.insert(userStoreMapping.toEntity())
            syncOutboxWriter.enqueueInsert(Tables.USER_STORE_MAPPING, userStoreMapping.toDto())
        }

        dataStore.edit { prefs ->
            prefs[HAS_STORE_KEY] = true
        }

        return storeId
    }
}
