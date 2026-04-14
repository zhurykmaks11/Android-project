package com.example.laba5.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.laba5.model.DetailState
import com.example.laba5.repository.AppRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class DetailViewModel(
    private val id: Int
) : ViewModel() {

    private val _state = MutableStateFlow<DetailState>(DetailState.Loading)
    val state: StateFlow<DetailState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            AppRepository.transactions.collect { list ->

                val item = list.find { it.id == id }

                _state.value = if (item != null) {
                    DetailState.Success(item)
                } else {
                    DetailState.Error("Елемент не знайдено")
                }
            }
        }
    }
}