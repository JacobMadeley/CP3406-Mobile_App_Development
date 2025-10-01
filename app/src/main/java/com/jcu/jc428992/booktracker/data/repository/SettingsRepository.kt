package com.jcu.jc428992.booktracker.data.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences // Use this import
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

enum class AppTheme {
    SYSTEM, LIGHT, DARK
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Singleton
class SettingsRepository @Inject constructor(@param:ApplicationContext private val context: Context) {

    private object Keys {
        val APP_THEME = stringPreferencesKey("app_theme")
    }

    val appTheme: Flow<AppTheme> = context.dataStore.data
        .map { preferences ->
            AppTheme.valueOf(preferences[Keys.APP_THEME] ?: AppTheme.SYSTEM.name)
        }

    suspend fun setAppTheme(theme: AppTheme) {
        context.dataStore.edit { settings ->
            settings[Keys.APP_THEME] = theme.name
        }
    }
}
