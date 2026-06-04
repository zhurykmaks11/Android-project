package com.example.laba5.repository

import com.example.laba5.data.local.TransactionDao
import com.example.laba5.data.local.TransactionEntity
import com.example.laba5.data.remote.RetrofitInstance
import com.example.laba5.data.remote.dto.TransactionDto
import com.example.laba5.model.Transaction
import com.example.laba5.model.Type
import com.example.laba5.model.Category

class TransactionRepository(
    private val dao: TransactionDao
) {

    private val api = RetrofitInstance.api

    suspend fun getAll(): List<Transaction> {
        return try {
            val networkData = api.getAll()

            val entities = networkData.map { dto ->
                TransactionEntity(
                    id = dto.id ?: 0,
                    amount = dto.amount,
                    type = Type.valueOf(dto.type.uppercase()),
                    category = Category.valueOf(dto.category.uppercase()),
                    isFavorite = false,
                    imagePath = null // З мережі фото не приходять
                )
            }

            dao.insertAll(entities)

            entities.map {
                Transaction(
                    id = it.id,
                    amount = it.amount,
                    type = it.type,
                    category = it.category,
                    isFavorite = it.isFavorite,
                    imagePath = it.imagePath
                )
            }

        } catch (e: Exception) {
            dao.getAllOnce().map {
                Transaction(
                    id = it.id,
                    amount = it.amount,
                    type = it.type,
                    category = it.category,
                    isFavorite = it.isFavorite,
                    imagePath = it.imagePath
                )
            }
        }
    }

    suspend fun getById(id: Int): Transaction {
        return try {
            // Пробуємо дістати з інтернету
            val dto = api.getById(id)
            Transaction(
                id = dto.id ?: 0,
                amount = dto.amount,
                type = Type.valueOf(dto.type.uppercase()),
                category = Category.valueOf(dto.category.uppercase()),
                isFavorite = false,
                imagePath = null
            )
        } catch (e: Exception) {
            val localEntity = dao.getById(id)
                ?: throw Exception("Транзакцію не знайдено ні в мережі, ні в базі")

            Transaction(
                id = localEntity.id,
                amount = localEntity.amount,
                type = localEntity.type,
                category = localEntity.category,
                isFavorite = localEntity.isFavorite,
                imagePath = localEntity.imagePath // Витягуємо фото з локальної бази
            )
        }
    }

    suspend fun insert(transaction: Transaction) {

        try {
            val created = api.create(
                TransactionDto(
                    amount = transaction.amount,
                    type = transaction.type.name,
                    category = transaction.category.name
                )
            )

            dao.insertAll(
                listOf(
                    TransactionEntity(
                        id = created.id ?: System.currentTimeMillis().toInt(),
                        amount = created.amount,
                        type = Type.valueOf(created.type.uppercase()),
                        category = Category.valueOf(created.category.uppercase()),
                        isFavorite = false,
                        imagePath = transaction.imagePath // Зберігаємо фото в БД
                    )
                )
            )

        } catch (e: Exception) {

            dao.insertAll(
                listOf(
                    TransactionEntity(
                        id = System.currentTimeMillis().toInt(),
                        amount = transaction.amount,
                        type = transaction.type,
                        category = transaction.category,
                        isFavorite = false,
                        imagePath = transaction.imagePath // Зберігаємо фото в БД без інтернету
                    )
                )
            )
        }
    }

    suspend fun delete(id: Int) {
        try {
            api.delete(id)
        } catch (e: Exception) {
        }

        dao.deleteById(id)
    }

    suspend fun updateFavorite(id: Int, isFavorite: Boolean) {
        dao.updateFavorite(id, isFavorite)
    }
}