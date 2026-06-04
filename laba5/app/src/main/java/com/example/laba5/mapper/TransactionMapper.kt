package com.example.laba5.mapper

import com.example.laba5.data.local.TransactionEntity
import com.example.laba5.data.remote.dto.TransactionDto
import com.example.laba5.model.Category
import com.example.laba5.model.Transaction
import com.example.laba5.model.Type

fun TransactionEntity.toDomain(): Transaction {
    return Transaction(
        id = id,
        amount = amount,
        type = type,
        category = category,
        isFavorite = isFavorite,
        imagePath = imagePath

    )
}

fun TransactionDto.toDomain(): Transaction {
    return Transaction(
        id = id ?: 0,
        amount = amount,
        type = Type.valueOf(type),
        category = Category.valueOf(category),
        isFavorite = false,
        imagePath = null
    )
}

fun Transaction.toDto(): TransactionDto {
    return TransactionDto(
        amount = amount,
        type = type.name,
        category = category.name
    )
}
fun Transaction.toEntity(): TransactionEntity {
    return TransactionEntity(
        id = id,
        amount = amount,
        type = type,
        category = category,
        isFavorite = isFavorite,
        imagePath = imagePath
    )
}