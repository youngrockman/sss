package com.example.shoesapptest.data.local

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class DataStoreForSlides(private val context: Context) {
    private val ONBOARDING_KEY = booleanPreferencesKey("onboarding_completed")
    private val dataStore = context.dataStore

    suspend fun setOnboardingCompleted(completed: Boolean) {
        dataStore.edit { settings ->
            settings[ONBOARDING_KEY] = completed
        }
    }

    val onboardingCompleted: Flow<Boolean> = dataStore.data
        .map { preferences ->
            preferences[ONBOARDING_KEY] ?: false
        }
}


