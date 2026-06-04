package com.example.lab1.models
import com.example.lab1.models.Category
class Expense(
    id: Int,
    amount: Double,
    description: String? = null,
    var category: Category
) : Transaction(id, amount, description) {

    override fun getInfo(): String {
        return "Expense: $amount, category: $category"
    }
}