package com.example.laba5.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun OnboardingScreen(navController: NavController) {

    var name by remember { mutableStateOf("") }

    val savedName = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<String>("name")


    savedName?.let {
        name = it
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text("Finance App", style = MaterialTheme.typography.headlineMedium)

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("name_input")
        }) {
            Text("Ввести ім'я", style = MaterialTheme.typography.titleLarge)
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            enabled = name.isNotEmpty(),
            onClick = {
                navController.navigate("main/$name") {
                    popUpTo("onboarding") { inclusive = true }
                }
            }
        ) {
            Text(
                if (name.isNotEmpty()) "Привіт, $name! Розпочати"
                else "Розпочати"
            )
        }
    }
}