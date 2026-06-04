package com.example.laba5.data

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "user_prefs")

class UserPreferences(private val context: Context) {

    companion object {
        val USER_NAME = stringPreferencesKey("user_name")
        val LANGUAGE = stringPreferencesKey("language")
        val DARK_MODE = booleanPreferencesKey("dark_mode")
        val SORT_ORDER = stringPreferencesKey("sort_order")
    }

    val sortOrderFlow: Flow<String> = context.dataStore.data
        .map { it[SORT_ORDER] ?: "ASC" }

    suspend fun saveSortOrder(order: String) {
        context.dataStore.edit {
            it[SORT_ORDER] = order
        }
    }
    val userNameFlow: Flow<String> = context.dataStore.data
        .map { it[USER_NAME] ?: "" }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { it[DARK_MODE] ?: false }


    val LANGUAGE = stringPreferencesKey("language")
    val THEME = booleanPreferencesKey("dark_theme")

    val languageFlow: Flow<String> = context.dataStore.data
        .map { it[LANGUAGE] ?: "UA" }

    val themeFlow: Flow<Boolean> = context.dataStore.data
        .map { it[THEME] ?: false }

    suspend fun saveLanguage(lang: String) {
        context.dataStore.edit {
            it[LANGUAGE] = lang
        }
    }

    suspend fun saveTheme(isDark: Boolean) {
        context.dataStore.edit {
            it[THEME] = isDark
        }
    }
    suspend fun saveUserName(name: String) {
        context.dataStore.edit { it[USER_NAME] = name }
    }


    suspend fun saveDarkMode(enabled: Boolean) {
        context.dataStore.edit { it[DARK_MODE] = enabled }
    }


}