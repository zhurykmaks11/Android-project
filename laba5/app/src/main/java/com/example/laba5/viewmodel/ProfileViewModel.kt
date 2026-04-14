package com.example.laba5.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.*

class ProfileViewModel : ViewModel() {

    private val _userName = MutableStateFlow("Гість")
    val userName: StateFlow<String> = _userName.asStateFlow()

    private val _language = MutableStateFlow("UA")
    val language: StateFlow<String> = _language.asStateFlow()

    fun updateName(name: String) {
        _userName.value = name
    }

    fun changeLanguage(lang: String) {
        _language.value = lang
    }
}