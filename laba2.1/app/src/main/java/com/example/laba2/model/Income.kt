package com.example.laba2.model

class Income(
    override val amount: Double,
    override val note: String? = null
) : Transaction(amount, note) {

    override fun getInfo(): String {
        return "Дохід: $amount грн"
    }
}