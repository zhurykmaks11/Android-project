package com.example.laba5.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import com.example.laba5.model.Transaction

@Composable
fun GridScreen(transactions: List<Transaction>) {

    var sortAsc by remember { mutableStateOf(true) }


    val sortedList by remember {
        derivedStateOf {
            if (sortAsc) {
                transactions.sortedBy { it.amount }
            } else {
                transactions.sortedByDescending { it.amount }
            }
        }
    }

    Column {

        Button(
            onClick = { sortAsc = !sortAsc },
            modifier = Modifier.padding(10.dp)
        ) {
            Text(if (sortAsc) "Сортування ↑" else "Сортування ↓")
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(10.dp)
        ) {
            items(sortedList.size) { i ->
                val t = sortedList[i]

                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Text("Тип: ${t.type}", style = MaterialTheme.typography.bodyMedium)
                        Text("Сума: ${t.amount}", style = MaterialTheme.typography.bodyMedium)
                        Text("Категорія: ${t.category}", style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}