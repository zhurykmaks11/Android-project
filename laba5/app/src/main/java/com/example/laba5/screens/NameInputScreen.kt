package com.example.laba5.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.compose.ui.text.input.KeyboardType
import com.example.laba5.viewmodel.UserViewModel

@Composable
fun NameInputScreen(navController: NavController, userViewModel: UserViewModel) {

    var text by remember { mutableStateOf("") }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        TextField(
            value = text,
            onValueChange = { text = it },
            label = { Text("Введіть ім’я", fontSize = 25.sp) },
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Text
            )
        )



        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            userViewModel.saveName(text)

            navController.navigate("main") {
                popUpTo("onboarding") { inclusive = true }
            }
        }) {
            Text("Continue")
        }
    }
}