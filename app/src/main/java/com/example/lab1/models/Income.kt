package com.example.lab1.models

class Income(
    id: Int,
    amount: Double,
    description: String? = null,
    var source: String = "Unknown"
) : Transaction(id, amount, description) {

    override fun getInfo(): String {
        return "Income: $amount from $source"
    }
}