package com.example.laba5.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.laba5.data.local.AppDatabase
import com.example.laba5.model.DetailState
import com.example.laba5.repository.TransactionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel(
    app: Application,
    private val id: Int
) : AndroidViewModel(app) {

    private val dao = AppDatabase.getDatabase(app).transactionDao()
    private val repository = TransactionRepository(dao)

    private val _state = MutableStateFlow<DetailState>(DetailState.Loading)
    val state: StateFlow<DetailState> = _state

    init {
        viewModelScope.launch {
            try {
                // Викликаємо оновлений метод з репозиторію
                val item = repository.getById(id)
                _state.value = DetailState.Success(item)
            } catch (e: Exception) {
                // Виводимо текст помилки в консоль, щоб легше шукати баги
                e.printStackTrace()
                _state.value = DetailState.Error("Помилка: ${e.message}")
            }
        }
    }
}