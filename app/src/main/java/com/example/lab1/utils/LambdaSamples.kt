package com.example.lab1.utils

import com.example.lab1.core.FinanceManager
import com.example.lab1.models.Expense
import com.example.lab1.models.Transaction

val filterBigExpenses: (Transaction) -> Boolean = { Transaction ->
    Transaction is Expense && Transaction.amount > 500
}

fun lambdaExample() {
    val result = FinanceManager.getAll().filter(filterBigExpenses)
    println(result)
}