package com.example.laba5.model

data class Transaction(
    val id: Int,
    val amount: Double,
    val type: Type,
    val category: Category
)