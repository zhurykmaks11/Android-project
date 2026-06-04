package com.example.laba5.screens

import android.app.Application
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.laba5.model.DetailState
import com.example.laba5.viewmodel.DetailViewModel
import com.example.laba5.viewmodel.DetailViewModelFactory

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(id: Int) {

    val context = LocalContext.current
    val app = context.applicationContext as Application

    val viewModel: DetailViewModel = viewModel(
        factory = DetailViewModelFactory(app, id)
    )

    val state by viewModel.state.collectAsState()

    var expanded by remember { mutableStateOf(false) }

    val rotation by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f,
        label = ""
    )

    val headerColor by animateColorAsState(
        targetValue = if (expanded)
            MaterialTheme.colorScheme.primaryContainer
        else
            MaterialTheme.colorScheme.surfaceVariant,
        label = ""
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text("Деталі транзакції")
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            when (state) {

                is DetailState.Loading -> {

                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is DetailState.Error -> {

                    Text(
                        text = "❌ Елемент не знайдено",
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                is DetailState.Success -> {

                    val item =
                        (state as DetailState.Success).transaction

                    Column {

                        Text(
                            text = "Сума: ${item.amount} грн",
                            style =
                                MaterialTheme.typography.headlineSmall
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Тип: ${item.type}"
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Категорія: ${item.category}"
                        )

                        Spacer(modifier = Modifier.height(16.dp))


                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(headerColor)
                                .clickable {
                                    expanded = !expanded
                                }
                                .padding(16.dp),

                            horizontalArrangement =
                                Arrangement.SpaceBetween,

                            verticalAlignment =
                                Alignment.CenterVertically
                        ) {

                            Text(
                                text = "Додаткова інформація",
                                style =
                                    MaterialTheme.typography.titleMedium
                            )

                            Icon(
                                imageVector =
                                    Icons.Default.KeyboardArrowDown,

                                contentDescription = null,

                                modifier = Modifier.rotate(rotation)
                            )
                        }


                        AnimatedVisibility(
                            visible = expanded,

                            enter = fadeIn() + expandVertically(),

                            exit = fadeOut() + shrinkVertically()
                        ) {

                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp)
                            ) {

                                Text("ID: ${item.id}")

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Text(
                                    text =
                                        if (item.isFavorite)
                                            "⭐ В обраному"
                                        else
                                            "☆ Не в обраному"
                                )

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Text(
                                    text =
                                        "Дата: 2025-06-02"
                                )

                                Spacer(
                                    modifier = Modifier.height(8.dp)
                                )

                                Text(
                                    text =
                                        "Статус: Активна транзакція"
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}