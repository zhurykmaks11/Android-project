package com.example.laba2.data

import com.example.laba2.model.*

val transactions = listOf(
    Expense(500.0, Category.FOOD),
    Expense(1200.0, Category.BILLS),
    Income(3000.0),
    Expense(200.0, Category.TRANSPORT),
    Expense(800.0, Category.ENTERTAINMENT)
)

val categories = setOf(
    Category.FOOD,
    Category.BILLS,
    Category.TRANSPORT,
    Category.ENTERTAINMENT
)

val categoryNames = mapOf(
    Category.FOOD to "Їжа",
    Category.BILLS to "Рахунки",
    Category.TRANSPORT to "Транспорт",
    Category.ENTERTAINMENT to "Розваги"
)


val bigExpensesSorted = transactions
    .filter { it is Expense && it.amount > 300  }
    .sortedBy { it.amount }

val amounts = transactions.map { it.amount }

val sortedTransactions = transactions.sortedBy { it.amount }

val grouped = transactions.groupBy {
    if (it is Expense) it.category else "Income"
}
