package com.example.laba5.data.remote.dto

data class TransactionDto(
    val id: Int? = null,
    val amount: Double,
    val type: String,
    val category: String
)