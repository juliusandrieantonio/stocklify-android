package com.scalo.stocklify.feature.onboarding.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.scalo.stocklify.core.util.Constants
import com.scalo.stocklify.feature.onboarding.domain.repository.OnboardingRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class OnboardingRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : OnboardingRepository {

    private val onboardingKey = booleanPreferencesKey(Constants.KEY_ONBOARDING_COMPLETED)

    override suspend fun hasCompletedOnboarding(): Boolean {
        return dataStore.data.first()[onboardingKey] ?: false
    }

    override suspend fun completeOnboarding() {
        dataStore.edit { preferences ->
            preferences[onboardingKey] = true
        }
    }
}