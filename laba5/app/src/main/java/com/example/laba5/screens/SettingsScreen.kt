package com.example.laba5.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.laba5.viewmodel.UserViewModel

@Composable
fun SettingsScreen(viewModel: UserViewModel) {

    val sortOrder by viewModel.sortOrder.collectAsState()
    val darkMode by viewModel.darkMode.collectAsState()

    Column {

        Text("Sort Order")

        Button(onClick = {
            viewModel.setSortOrder("ASC")
        }) {
            Text("ASC")
        }

        Button(onClick = {
            viewModel.setSortOrder("DESC")
        }) {
            Text("DESC")
        }


        Spacer(modifier = Modifier.height(16.dp))

        Row {
            Text("Dark Mode")
            Switch(
                checked = darkMode,
                onCheckedChange = {
                    viewModel.setDarkMode(it)
                }
            )
        }
    }
}