package com.example.laba5.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.laba5.data.UserPreferences
import com.example.laba5.data.local.AppDatabase
import com.example.laba5.model.*
import com.example.laba5.repository.TransactionRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.io.File

class ListViewModel(app: Application) : AndroidViewModel(app) {

    private val dao = AppDatabase.getDatabase(app).transactionDao()
    private val repository = TransactionRepository(dao)
    private val prefs = UserPreferences(app)

    private val _state = MutableStateFlow<UiState>(UiState.Loading)
    val state = _state.asStateFlow()

    private val _filterCategory = MutableStateFlow<Category?>(null)
    val filterCategory = _filterCategory.asStateFlow()

    private val _showFavorites = MutableStateFlow(false)
    val showFavorites = _showFavorites.asStateFlow()

    private val _event = MutableSharedFlow<String>()
    val event = _event.asSharedFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.value = UiState.Loading
            try {
                val data = repository.getAll()
                _state.value = UiState.Success(data)
            } catch (e: Exception) {
                _state.value = UiState.Error("❌ Немає інтернету")
            }
        }
    }

    fun getTransactionById(id: Int): Transaction? {
        return finalList.value.find { it.id == id }
    }

    // ОНОВЛЕНО: Тепер приймає imagePath
    fun addTransaction(amount: Double, type: Type, category: Category, imagePath: String?) {
        viewModelScope.launch {
            try {
                repository.insert(
                    Transaction(
                        id = 0,
                        amount = amount,
                        type = type,
                        category = category,
                        isFavorite = false,
                        imagePath = imagePath // Зберігаємо шлях у БД
                    )
                )
                load()
                _event.emit("✅ Додано")
            } catch (e: Exception) {
                _event.emit("❌ Не вдалося додати")
            }
        }
    }

    fun toggleFavorite(id: Int, current: Boolean) {
        viewModelScope.launch {
            repository.updateFavorite(id, !current)
            load()
        }
    }

    // ОНОВЛЕНО: Тепер видаляє не лише з БД, але й фізичний файл
    fun delete(id: Int) {
        viewModelScope.launch {
            try {
                // Знаходимо транзакцію, щоб отримати шлях до її фото
                val transactionToDelete = finalList.value.find { it.id == id }

                // Видаляємо з БД/Сервера
                repository.delete(id)

                // Фізично видаляємо файл із внутрішнього сховища (Вимога ЛР №12)
                transactionToDelete?.imagePath?.let { path ->
                    val file = File(path)
                    if (file.exists()) {
                        file.delete()
                    }
                }

                load()
                _event.emit("🗑 Видалено")
            } catch (e: Exception) {
                _event.emit("❌ Не вдалося видалити")
            }
        }
    }

    fun toggleShowFavorites() {
        _showFavorites.value = !_showFavorites.value
    }

    fun setFilter(category: Category?) {
        _filterCategory.value = category
    }

    fun toggleSort() {
        viewModelScope.launch {
            val current = prefs.sortOrderFlow.first()
            val newOrder = if (current == "ASC") "DESC" else "ASC"
            prefs.saveSortOrder(newOrder)
            load()
        }
    }

    val finalList = combine(
        state,
        _filterCategory,
        _showFavorites,
        prefs.sortOrderFlow
    ) { state, filter, showFav, sortOrder ->

        if (state !is UiState.Success) return@combine emptyList()

        var list = state.data

        if (showFav) {
            list = list.filter { it.isFavorite }
        }

        if (filter != null) {
            list = list.filter { it.category == filter }
        }

        list = if (sortOrder == "ASC") {
            list.sortedBy { it.amount }
        } else {
            list.sortedByDescending { it.amount }
        }

        list
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        emptyList()
    )
}