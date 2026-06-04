package com.example.laba5.model

sealed interface DetailState {
    object Loading : DetailState

    data class Success(val transaction: Transaction) : DetailState

    data class Error(val message: String) : DetailState
}