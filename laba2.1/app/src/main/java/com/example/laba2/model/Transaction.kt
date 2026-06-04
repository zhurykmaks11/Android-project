package com.example.laba2.model


open class Transaction(
    open val amount: Double,
    open val note: String? = null
) {
    open fun getInfo(): String {
        return "Сума: $amount"
    }

    fun printInfo() {
        println(getInfo())
    }
}