package com.example.laba5.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.laba5.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(viewModel: ProfileViewModel) {

    val name by viewModel.userName.collectAsState()
    val language by viewModel.language.collectAsState()

    Column(modifier = Modifier.padding(16.dp)) {

        Text(
            "Профіль",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedTextField(
            value = name,
            onValueChange = { viewModel.updateName(it) },
            label = { Text("Ім’я") }
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text("Мова: $language")

        Row {
            Button(onClick = { viewModel.changeLanguage("Українська") }) {
                Text("UA")
            }

            Spacer(modifier = Modifier.width(8.dp))

            Button(onClick = { viewModel.changeLanguage("English") }) {
                Text("EN")
            }
        }
    }
}