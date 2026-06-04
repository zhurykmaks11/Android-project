package com.example.laba5.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.laba5.model.Category
import com.example.laba5.model.Type
import com.example.laba5.model.UiState
import com.example.laba5.viewmodel.ListViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalFoundationApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun ListScreen(
    viewModel: ListViewModel,
    navController: NavHostController
) {
    val state by viewModel.state.collectAsState()
    val list by viewModel.finalList.collectAsState()
    val showFavorites by viewModel.showFavorites.collectAsState()

    var amountText by remember { mutableStateOf("") }
    var selectedType by remember { mutableStateOf(Type.EXPENSE) }
    var selectedCategory by remember { mutableStateOf(Category.OTHER) }

    // Стан для випадаючого списку категорій
    var isCategoryExpanded by remember { mutableStateOf(false) }

    val snackbarHostState = remember { SnackbarHostState() }
    var refreshing by remember { mutableStateOf(false) }

    var expandedMenuId by remember { mutableStateOf<Int?>(null) }

    val pullRefreshState = rememberPullRefreshState(
        refreshing = refreshing,
        onRefresh = { refreshing = true }
    )

    LaunchedEffect(refreshing) {
        if (refreshing) {
            delay(1500)
            viewModel.load()
            refreshing = false
        }
    }

    LaunchedEffect(Unit) {
        viewModel.event.collectLatest {
            snackbarHostState.showSnackbar(it)
        }
    }

    val balance = remember(list) {
        list.sumOf {
            if (it.type == Type.INCOME) it.amount else -it.amount
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .pullRefresh(pullRefreshState)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {

                item {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Баланс: $balance грн",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    if (state is UiState.Error && list.isNotEmpty()) {
                        Text("⚠️ Офлайн режим", color = MaterialTheme.colorScheme.error)
                    }

                    if (state is UiState.Loading) {
                        CircularProgressIndicator(modifier = Modifier.padding(vertical = 8.dp))
                    }

                    if (state is UiState.Error && list.isEmpty()) {
                        Column {
                            Text("❌ Помилка завантаження")
                            Button(onClick = { viewModel.load() }) {
                                Text("Повторити")
                            }
                        }
                    }

                    if (state is UiState.Success) {
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = amountText,
                            onValueChange = {
                                amountText = it.filter { ch -> ch.isDigit() || ch == '.' }
                            },
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

                        Spacer(modifier = Modifier.height(16.dp))

                        ExposedDropdownMenuBox(
                            expanded = isCategoryExpanded,
                            onExpandedChange = { isCategoryExpanded = !isCategoryExpanded },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedCategory.name,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Категорія") },
                                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isCategoryExpanded) },
                                colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
                                modifier = Modifier
                                    // ВИПРАВЛЕНО ТУТ: Використання нового перевантаження menuAnchor
                                    .menuAnchor(type = MenuAnchorType.PrimaryNotEditable, enabled = true)
                                    .fillMaxWidth()
                            )
                            ExposedDropdownMenu(
                                expanded = isCategoryExpanded,
                                onDismissRequest = { isCategoryExpanded = false }
                            ) {
                                Category.entries.forEach { category ->
                                    DropdownMenuItem(
                                        text = { Text(category.name) },
                                        onClick = {
                                            selectedCategory = category
                                            isCategoryExpanded = false
                                        }
                                    )
                                }
                            }
                        }


                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val amount = amountText.toDoubleOrNull()
                                if (amount != null) {
                                    viewModel.addTransaction(amount, selectedType, selectedCategory, null)
                                    amountText = ""
                                }
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Додати")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { navController.navigate("add") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("➕ Додати транзакцію")
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                navController.navigate("camera")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("📷 Камера")
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                navController.navigate("location")
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("📍 Геолокація")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Button(onClick = { viewModel.toggleSort() }) {
                                Text("Сортувати")
                            }
                            Button(onClick = { viewModel.toggleShowFavorites() }) {
                                Text(if (showFavorites) "Всі" else "⭐ Обрані")
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }

                if (state is UiState.Success) {
                    items(
                        items = list,
                        key = { it.id }
                    ) { t ->

                        val dismissState = rememberSwipeToDismissBoxState(
                            confirmValueChange = {
                                if (it == SwipeToDismissBoxValue.EndToStart) {
                                    viewModel.delete(t.id)
                                    true
                                } else false
                            }
                        )

                        AnimatedVisibility(
                            visible = true,
                            enter = fadeIn() + expandVertically(),
                            exit = fadeOut() + shrinkVertically()
                        ) {
                            SwipeToDismissBox(
                                state = dismissState,
                                backgroundContent = {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(vertical = 4.dp)
                                            .background(MaterialTheme.colorScheme.errorContainer, shape = MaterialTheme.shapes.medium)
                                            .padding(horizontal = 16.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        Text("🗑 Видалити", color = MaterialTheme.colorScheme.onErrorContainer)
                                    }
                                },
                                content = {
                                    Card(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                    ) {
                                        Box {
                                            Row(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .combinedClickable(
                                                        onClick = { navController.navigate("detail/${t.id}") },
                                                        onLongClick = { expandedMenuId = t.id }
                                                    )

                                                    .semantics(mergeDescendants = true) {}
                                                    .padding(16.dp),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text(
                                                        "${t.amount} грн",
                                                        style = MaterialTheme.typography.titleMedium,
                                                        color = if (t.type == Type.INCOME) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                                                    )
                                                    Text(t.category.name, style = MaterialTheme.typography.bodyMedium)
                                                }

                                                IconButton(
                                                    onClick = { viewModel.toggleFavorite(t.id, t.isFavorite) },

                                                    modifier = Modifier.clearAndSetSemantics { }
                                                ) {
                                                    Text(if (t.isFavorite) "⭐" else "☆")
                                                }
                                            }

                                            DropdownMenu(
                                                expanded = expandedMenuId == t.id,
                                                onDismissRequest = { expandedMenuId = null }
                                            ) {
                                                DropdownMenuItem(
                                                    text = { Text(if (t.isFavorite) "Видалити з обраного" else "Додати в обране") },
                                                    onClick = {
                                                        viewModel.toggleFavorite(t.id, t.isFavorite)
                                                        expandedMenuId = null
                                                    }
                                                )
                                                DropdownMenuItem(
                                                    text = { Text("Видалити транзакцію", color = MaterialTheme.colorScheme.error) },
                                                    onClick = {
                                                        viewModel.delete(t.id)
                                                        expandedMenuId = null
                                                    }
                                                )
                                            }
                                        }
                                    }
                                }
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(80.dp)) }
            }

            PullRefreshIndicator(
                refreshing = refreshing,
                state = pullRefreshState,
                modifier = Modifier.align(Alignment.TopCenter)
            )
        }
    }
}