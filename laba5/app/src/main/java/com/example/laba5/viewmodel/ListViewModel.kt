package com.example.laba5.viewmodel

import androidx.lifecycle.ViewModel
import com.example.laba5.model.Category
import com.example.laba5.model.Type
import com.example.laba5.repository.AppRepository
import kotlinx.coroutines.flow.*

class ListViewModel : ViewModel() {

    private val repository = AppRepository

    private val _filterCategory = MutableStateFlow<Category?>(null)
    val filterCategory: StateFlow<Category?> = _filterCategory

    private val _sortAsc = MutableStateFlow(true)

    val filteredSortedList = combine(
        repository.transactions,
        _filterCategory,
        _sortAsc
    ) { list, category, asc ->

        val filtered = if (category == null) list
        else list.filter { it.category == category }

        if (asc) filtered.sortedBy { it.amount }
        else filtered.sortedByDescending { it.amount }

    }.stateIn(
        scope = kotlinx.coroutines.GlobalScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun addTransaction(amount: Double, type: Type, category: Category) {
        repository.addTransaction(amount, type, category)
    }

    fun setFilter(category: Category?) {
        _filterCategory.value = category
    }

    fun toggleSort() {
        _sortAsc.value = !_sortAsc.value
    }
}