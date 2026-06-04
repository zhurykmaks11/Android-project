package com.example.laba5.data.remote

import com.example.laba5.data.remote.dto.TransactionDto
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    @GET("transactions")
    suspend fun getAll(): List<TransactionDto>

    @GET("transactions/{id}")
    suspend fun getById(@Path("id") id: Int): TransactionDto

    @POST("transactions")
    suspend fun create(@Body item: TransactionDto): TransactionDto

    @DELETE("transactions/{id}")
    suspend fun delete(@Path("id") id: Int)
}