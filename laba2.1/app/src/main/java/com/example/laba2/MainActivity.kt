package com.example.laba2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.Alignment
import kotlinx.coroutines.delay

data class ExpenseItem(
    val amount: Double,
    val category: String
)


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {

    var text by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("FOOD") }
    val expenses = remember { mutableStateListOf<ExpenseItem>() }

    var isLoading by remember { mutableStateOf(true) }
    var sortAsc by remember { mutableStateOf(true) }

    val categories = listOf("FOOD", "TRANSPORT", "ENTERTAINMENT", "BILLS", "OTHER")

    LaunchedEffect(Unit) {
        delay(1500)
        expenses.addAll(
            listOf(
                ExpenseItem(500.0, "FOOD"),
                ExpenseItem(800.0, "ENTERTAINMENT"),
                ExpenseItem(1200.0, "BILLS")
            )
        )
        isLoading = false
    }

    val sortedList by remember(sortAsc, expenses) {
        derivedStateOf {
            if (sortAsc) {
                expenses.sortedBy { it.amount }
            } else {
                expenses.sortedByDescending { it.amount }
            }
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Spacer(modifier = Modifier.height(25.dp))
        Text("Список витрат", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.height(8.dp))

        InputPanel(
            text = text,
            selectedCategory = selectedCategory,
            categories = categories,
            onTextChange = { text = it },
            onCategoryChange = { selectedCategory = it },
            onAdd = {
                val amount = text.toDoubleOrNull()
                if (amount != null) {
                    expenses.add(ExpenseItem(amount, selectedCategory))
                    text = ""
                }
            }
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Кількість: ${expenses.size}")

        if (expenses.size > 5) {
            Text("Забагато елементів!", color = Color.Red)
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { sortAsc = !sortAsc }) {
            Text(if (sortAsc) "Сортувати ↓" else "Сортувати ↑")
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (isLoading) {
            CircularProgressIndicator()
        } else {

            if (expenses.isEmpty()) {
                Text("Список порожній")
            } else {
                LazyColumn(
                    modifier = Modifier.height(250.dp)
                ) {
                    items(sortedList) { item ->
                        ItemRow(
                            item = item,
                            onDelete = { expenses.remove(item) }
                        )
                    }
                }
            }
        }


        Spacer(modifier = Modifier.height(16.dp))

        Text("Категорії", style = MaterialTheme.typography.titleMedium)

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.padding(top = 8.dp)
        ) {
            items(categories) { category ->
                Box(
                    modifier = Modifier
                        .padding(8.dp)
                        .size(120.dp)
                        .background(Color.Cyan),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(category)
                        Text("Категорія")
                    }
                }
            }
        }
    }
}

@Composable
fun InputPanel(
    text: String,
    selectedCategory: String,
    categories: List<String>,
    onTextChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onAdd: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Column {
        TextField(
            value = text,
            onValueChange = onTextChange,
            label = { Text("Введіть суму") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Button(onClick = { expanded = true }) {
                Text("Категорія: $selectedCategory")
            }

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                categories.forEach { category ->
                    DropdownMenuItem(
                        text = { Text(category) },
                        onClick = {
                            onCategoryChange(category)
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = onAdd) {
            Text("Додати")
        }
    }
}

@Composable
fun ItemRow(
    item: ExpenseItem,
    onDelete: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text("Витрата: ${item.amount} грн")
            Text("Категорія: ${item.category}")
        }

        Button(onClick = onDelete) {
            Text("X")
        }
    }
}