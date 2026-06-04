package com.example.lab1.extensions

import com.example.lab1.core.FinanceManager
import com.example.lab1.models.Income
import com.example.lab1.models.Transaction

fun Transaction.isBig(): Boolean {
    return this.amount > 1000
}

fun FinanceManager.totalIncome(): Double {
    return getAll().filterIsInstance<Income>().sumOf { it.amount }
}