package com.example.laba5.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.laba5.data.UserPreferences
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(app: Application) : AndroidViewModel(app) {

    private val prefs = UserPreferences(app)

    val userName = prefs.userNameFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        ""
    )

    val language = prefs.languageFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "UA"
    )

    val isDarkTheme = prefs.themeFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        false
    )
    fun setSort(order: String) {
        viewModelScope.launch {
            prefs.saveSortOrder(order)
        }
    }
    val sortOrder = prefs.sortOrderFlow.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        "ASC"
    )

    fun toggleSort() {
        viewModelScope.launch {
            val newOrder = if (sortOrder.value == "ASC") "DESC" else "ASC"
            prefs.saveSortOrder(newOrder)
        }
    }

    fun updateName(name: String) {
        viewModelScope.launch {
            prefs.saveUserName(name)
        }
    }

    fun changeLanguage(lang: String) {
        viewModelScope.launch {
            prefs.saveLanguage(lang)
        }
    }

    fun toggleTheme() {
        viewModelScope.launch {
            prefs.saveTheme(!isDarkTheme.value)
        }
    }
}
