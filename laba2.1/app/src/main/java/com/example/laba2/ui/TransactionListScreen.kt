package com.example.laba2.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.laba2.data.bigExpensesSorted
import com.example.laba2.ui.TransactionListItem

@Composable
fun TransactionListScreen() {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(8.dp)
    ) {
        items(bigExpensesSorted) { transaction ->
            TransactionListItem(transaction)
        }
    }
}