package com.example.laba5.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.laba5.model.Category
import com.example.laba5.model.Type

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val amount: Double,
    val type: Type,
    val category: Category,
    val isFavorite: Boolean = false,
    val imagePath: String? = null
)