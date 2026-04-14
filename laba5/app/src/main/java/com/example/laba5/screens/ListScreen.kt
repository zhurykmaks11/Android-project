package com.example.laba5.screens

import android.R.style
import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.laba5.model.Category
import com.example.laba5.model.Type
import com.example.laba5.viewmodel.ListViewModel
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import android.content.res.Configuration
import com.example.laba5.ui.theme.AppTheme

@Composable
fun ListScreen(viewModel: ListViewModel, navController: NavHostController) {

    val list by viewModel.filteredSortedList.collectAsState()
    val filter by viewModel.filterCategory.collectAsState()

    var amountText by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(Type.EXPENSE) }
    var selectedCategory by remember { mutableStateOf(Category.OTHER) }

    var categoryExpanded by remember { mutableStateOf(false) }
    var filterExpanded by remember { mutableStateOf(false) }

    val balance = list.sumOf {
        if (it.type == Type.INCOME) it.amount else -it.amount
    }

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            "Фінанси",
            style = MaterialTheme.typography.headlineMedium
        )

        Text(
            "Баланс: $balance грн",
            style = MaterialTheme.typography.titleLarge
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = amountText,
            onValueChange = { amountText = it },
            label = { Text("Сума") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row {
            Button(onClick = { selectedType = Type.EXPENSE }) {
                Text("Витрата")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { selectedType = Type.INCOME }) {
                Text("Дохід")
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Box {
            Button(onClick = { categoryExpanded = true }) {
                Text("Категорія: ${selectedCategory.name}")
            }

            DropdownMenu(
                expanded = categoryExpanded,
                onDismissRequest = { categoryExpanded = false }
            ) {
                Category.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(it.name) },
                        onClick = {
                            selectedCategory = it
                            categoryExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                val amount = amountText.toDoubleOrNull()
                if (amount != null) {
                    viewModel.addTransaction(amount, selectedType, selectedCategory)
                    amountText = ""
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Додати")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            "Фільтр",
            style = MaterialTheme.typography.titleMedium
        )

        Box {
            Button(onClick = { filterExpanded = true }) {
                Text(filter?.name ?: "Всі")
            }

            DropdownMenu(
                expanded = filterExpanded,
                onDismissRequest = { filterExpanded = false }
            ) {

                DropdownMenuItem(
                    text = { Text("Всі") },
                    onClick = {
                        viewModel.setFilter(null)
                        filterExpanded = false
                    }
                )

                Category.entries.forEach {
                    DropdownMenuItem(
                        text = { Text(it.name) },
                        onClick = {
                            viewModel.setFilter(it)
                            filterExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { viewModel.toggleSort() }) {
            Text("Сортувати")
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn {
            items(list) { t ->
                Text(
                    "${t.type} ${t.amount} грн (${t.category})",
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            navController.navigate("detail/${t.id}")
                        }
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true)
@Composable
fun ListScreenPreviewLight() {
    AppTheme(darkTheme = false) {
        ListScreen(
            viewModel = ListViewModel(),
            navController = rememberNavController()
        )
    }
}

@SuppressLint("ViewModelConstructorInComposable")
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ListScreenPreviewDark() {
    AppTheme(darkTheme = true) {
        ListScreen(
            viewModel = ListViewModel(),
            navController = rememberNavController()
        )
    }
}