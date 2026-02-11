package com.pixiemays.meownotes.Preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.pixiemays.meownotes.ui.theme.AppTheme
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class UserPreferencesRepository(private val context: Context) {

    companion object {
        private val THEME_KEY = stringPreferencesKey("app_theme")
        private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        private val FIRST_LAUNCH_KEY = booleanPreferencesKey("first_launch")
    }

    val userPreferencesFlow: Flow<UserPreferences> = context.dataStore.data
        .map { preferences ->
            UserPreferences(
                appTheme = AppTheme.valueOf(
                    preferences[THEME_KEY] ?: AppTheme.PURPLE.name
                ),
                isDarkMode = preferences[DARK_MODE_KEY] ?: false,
                isFirstLaunch = preferences[FIRST_LAUNCH_KEY] ?: true
            )
        }

    suspend fun updateTheme(theme: AppTheme) {
        context.dataStore.edit { preferences ->
            preferences[THEME_KEY] = theme.name
        }
    }

    suspend fun updateDarkMode(isDarkMode: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = isDarkMode
        }
    }

    suspend fun setFirstLaunchComplete() {
        context.dataStore.edit { preferences ->
            preferences[FIRST_LAUNCH_KEY] = false
        }
    }
}

data class UserPreferences(
    val appTheme: AppTheme,
    val isDarkMode: Boolean,
    val isFirstLaunch: Boolean
)