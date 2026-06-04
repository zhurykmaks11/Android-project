package com.example.laba5.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Query("SELECT * FROM transactions")
    fun getAll(): Flow<List<TransactionEntity>>
    @Query("SELECT * FROM transactions")
    suspend fun getAllOnce(): List<TransactionEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(list: List<TransactionEntity>)
    @Query("UPDATE transactions SET isFavorite = :isFav WHERE id = :id")
    suspend fun updateFavorite(id: Int, isFav: Boolean)
    @Insert
    suspend fun insert(transaction: TransactionEntity)

    @Delete
    suspend fun delete(transaction: TransactionEntity)

    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1")
    suspend fun getById(id: Int): TransactionEntity?
    @Query("DELETE FROM transactions WHERE id = :id")
    suspend fun deleteById(id: Int)
}