package com.example.lab1.models

open class Transaction(
    val id: Int,
    var amount: Double,
    var description: String? = null
) {

    open fun getInfo(): String {
        return "Transaction: $amount"
    }

    fun printInfo() {
        println(getInfo())
    }

    companion object {
        fun generateId(): Int {
            return (0..999999).random()
        }
    }
}