package com.scalo.stocklify.feature.premium.domain

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.scalo.stocklify.core.data.sync.SyncManager
import com.scalo.stocklify.core.util.constants.ConstantPreferences
import com.scalo.stocklify.feature.premium.domain.repository.OrganizationRepository
import javax.inject.Inject

class PremiumUseCase @Inject constructor(
    private val organizationRepository: OrganizationRepository,
    private val syncManager: SyncManager,
    private val dataStore: DataStore<Preferences>
) {

    companion object {
        val HAS_ORGANIZATION_KEY = booleanPreferencesKey(ConstantPreferences.HAS_ORGANIZATION_KEY)
    }

    suspend fun createOrganization(organizationName: String): String {
        val organizationId = organizationRepository.createOrganization(organizationName)

        // The org + owner membership were created server-side by the RPC; pull them into Room.
        syncManager.pull()

        dataStore.edit { prefs ->
            prefs[HAS_ORGANIZATION_KEY] = true
        }

        return organizationId
    }

    suspend fun joinOrganization(inviteCode: String): String {
        val organizationId = organizationRepository.joinOrganization(inviteCode)

        dataStore.edit { prefs ->
            prefs[HAS_ORGANIZATION_KEY] = true
        }

        return organizationId
    }
}
