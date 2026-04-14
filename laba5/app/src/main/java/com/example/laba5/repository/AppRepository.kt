package com.example.laba5.repository

import com.example.laba5.model.Category
import com.example.laba5.model.Transaction
import com.example.laba5.model.Type
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object AppRepository {

    private val _transactions = MutableStateFlow<List<Transaction>>(emptyList())
    val transactions: StateFlow<List<Transaction>> = _transactions.asStateFlow()

    private var currentId = 0

    fun addTransaction(amount: Double, type: Type, category: Category) {
        val newTransaction = Transaction(
            id = currentId++,
            amount = amount,
            type = type,
            category = category
        )

        // 🔥 ВАЖЛИВО
        _transactions.value = _transactions.value + newTransaction
    }

    fun getById(id: Int): Transaction? {
        return _transactions.value.find { it.id == id }
    }

    fun delete(transaction: Transaction) {
        _transactions.value = _transactions.value - transaction
    }

    fun getBalance(): Double {
        return _transactions.value.sumOf {
            if (it.type == Type.INCOME) it.amount else -it.amount
        }
    }
}