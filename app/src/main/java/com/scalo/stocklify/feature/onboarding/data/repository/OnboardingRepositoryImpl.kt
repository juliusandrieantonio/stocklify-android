package com.scalo.stocklify.feature.onboarding.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.scalo.stocklify.core.util.constants.ConstantPreferences
import com.scalo.stocklify.core.util.constants.Constants
import com.scalo.stocklify.feature.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : OnboardingRepository {

    private val onboardingKey = booleanPreferencesKey(ConstantPreferences.KEY_ONBOARDING_COMPLETED)
    private val welcomeScreenKey = booleanPreferencesKey(ConstantPreferences.KEY_WELCOME_SCREEN_COMPLETED)
    private val storeKey = booleanPreferencesKey(ConstantPreferences.HAS_STORE_KEY)
    private val organizationKey = booleanPreferencesKey(ConstantPreferences.HAS_ORGANIZATION_KEY)

    override suspend fun hasStoreCached(): Boolean {
        return dataStore.data.first()[storeKey] ?: false
    }

    override suspend fun hasOrganizationCached(): Boolean {
        return dataStore.data.first()[organizationKey] ?: false
    }

    override suspend fun hasCompletedOnboarding(): Boolean {
        return dataStore.data.first()[onboardingKey] ?: false
    }

    override suspend fun hasCompletedWelcomeScreen(): Boolean {
        return dataStore.data.first()[welcomeScreenKey] ?: false
    }

    override suspend fun completeOnboarding() {
        dataStore.edit { preferences ->
            preferences[onboardingKey] = true
        }
    }

    override suspend fun completeWelcomeScreen() {
        dataStore.edit { preferences ->
            preferences[welcomeScreenKey] = true
        }
    }

    override suspend fun setStoreCached(hasStore: Boolean) {
        dataStore.edit { preferences ->
            preferences[storeKey] = hasStore
        }
    }

    override suspend fun setOrganizationCached(hasOrganization: Boolean) {
        dataStore.edit { preferences ->
            preferences[organizationKey] = hasOrganization
        }
    }
}