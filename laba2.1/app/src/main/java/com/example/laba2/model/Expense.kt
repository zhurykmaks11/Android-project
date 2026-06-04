package com.example.laba2.model


class Expense : Transaction {

    val category: Category

    constructor(amount: Double, category: Category) : super(amount) {
        this.category = category
    }


    constructor(amount: Double, category: Category, note: String?) : super(amount, note) {
        this.category = category
    }

    override fun getInfo(): String {
        return "Витрата: $amount грн, \n Категорія: $category"
    }
}