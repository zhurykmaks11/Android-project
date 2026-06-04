package com.example.laba5.navigation

import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.laba5.screens.*
import com.example.laba5.viewmodel.ListViewModel
import com.example.laba5.viewmodel.UserViewModel

@Composable
fun NavGraph(navController: NavHostController, userViewModel: UserViewModel) {

    val name by userViewModel.userName.collectAsState(initial = null)

    val startDestination = if (name.isNullOrEmpty()) "onboarding" else "main"

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {

        composable("onboarding") {
            OnboardingScreen(navController)
        }

        composable("name_input") {
            NameInputScreen(navController, userViewModel)
        }

        composable("add") {
            val listVM: ListViewModel = viewModel()
            AddTransactionScreen(navController, listVM)
        }
        composable("main") {
            MainScreen(navController)
        }

        composable(
            "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("id") ?: 0
            DetailScreen(id)
        }

        composable("camera") {
            CameraScreen()
        }
        composable("location") {
            LocationScreen()
        }
    }
}