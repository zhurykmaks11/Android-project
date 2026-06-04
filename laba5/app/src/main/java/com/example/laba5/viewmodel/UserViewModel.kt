package com.example.laba5.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.laba5.data.UserPreferences
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = UserPreferences(app)

    val userName = prefs.userNameFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    val sortOrder = prefs.sortOrderFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "ASC"
    )

    val darkMode = prefs.darkModeFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )

    fun saveName(name: String) {
        viewModelScope.launch {
            prefs.saveUserName(name)
        }
    }

    fun setSortOrder(order: String) {
        viewModelScope.launch {
            prefs.saveSortOrder(order)
        }
    }

    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            prefs.saveDarkMode(enabled)
        }
    }
}