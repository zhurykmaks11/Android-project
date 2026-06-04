package com.example.laba5.screens

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.laba5.viewmodel.ListViewModel
import com.example.laba5.viewmodel.ProfileViewModel

@Composable
fun MainScreen(navController: NavHostController) {

    var selectedTab by remember { mutableStateOf(0) }

    val listVM: ListViewModel = viewModel()
    val profileVM: ProfileViewModel = viewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = { Text("Список") },
                    icon = {}
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = { Text("Плитка") },
                    icon = {}
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    label = { Text("Профіль") },
                    icon = {}
                )
            }
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(12.dp)
        ) {
            Text(
                "Finance App",
                style = MaterialTheme.typography.headlineMedium
            )


            when (selectedTab) {
                0 -> ListScreen(listVM, navController)
                1 -> {
                    val list by listVM.finalList.collectAsState()
                    GridScreen(list)
                }
                2 -> ProfileScreen(profileVM)
            }
        }
    }
}