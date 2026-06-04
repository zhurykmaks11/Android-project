package com.example.lab1.core

import com.example.lab1.models.Expense
import com.example.lab1.models.Income
import com.example.lab1.models.Transaction

object FinanceManager {

    private val transactions = mutableListOf<Transaction>()

    fun addTransaction(t: Transaction) {
        transactions.add(t)
    }

    fun getAll(): List<Transaction> {
        return transactions
    }

    fun getBalance(): Double {
        var balance = 0.0
        transactions.forEach {
            when(it) {
                is Income -> balance += it.amount
                is Expense -> balance -= it.amount
            }
        }
        return balance
    }
}