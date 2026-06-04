package com.example.laba5.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.laba5.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {

    val name by viewModel.userName.collectAsState()
    val language by viewModel.language.collectAsState()
    val isDarkTheme by viewModel.isDarkTheme.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        Text("Налаштування", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 ІМ'Я
        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text("Ім’я") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 МОВА
        Text("Мова: $language")

        Row {
            Button(onClick = { viewModel.changeLanguage("UA") }) {
                Text("UA")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(onClick = { viewModel.changeLanguage("EN") }) {
                Text("EN")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔹 ТЕМА
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Темна тема")
            Switch(
                checked = isDarkTheme,
                onCheckedChange = { viewModel.toggleTheme() }
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // 🔥 СОРТУВАННЯ (ЗАВДАННЯ 5)
        Text("Сортування за замовчуванням:")

        Row {
            Button(
                onClick = { viewModel.setSort("ASC") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (sortOrder == "ASC")
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondary
                )
            )
            {
                Text("↑ Зростання")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = { viewModel.setSort("DESC") },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (sortOrder == "DESC")
                        MaterialTheme.colorScheme.primary
                    else
                        MaterialTheme.colorScheme.secondary
                )
            ) {
                Text("↓ Спадання")
            }
        }
    }
}