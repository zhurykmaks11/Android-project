package com.example.laba5.model

sealed class UiState {
    object Loading : UiState()
    data class Success(val data: List<Transaction>) : UiState()
    data class Error(val message: String) : UiState()
}