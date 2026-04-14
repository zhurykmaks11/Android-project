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
fun MainScreen(userName: String, navController: NavHostController) {

    var selectedTab by remember { mutableStateOf(0) }

    val listVM: ListViewModel = viewModel()
    val profileVM: ProfileViewModel = viewModel()

    LaunchedEffect(Unit) {
        profileVM.updateName(userName)
    }
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = {
                        Text(
                            "Список",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    icon = {}
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = {
                        Text(
                            "Плитка",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    icon = {}
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    label = {
                        Text(
                            "Профіль",
                            style = MaterialTheme.typography.labelLarge
                        )
                    },
                    icon = {}
                )
            }
        }

    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Text(
                "Finance App",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            when (selectedTab) {
                0 -> ListScreen(listVM, navController)
                1 -> GridScreen(listVM.filteredSortedList.collectAsState().value)
                2 -> ProfileScreen(profileVM)
            }
        }
    }
}