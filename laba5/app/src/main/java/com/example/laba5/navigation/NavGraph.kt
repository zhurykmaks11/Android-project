package com.example.laba5.navigation

import androidx.compose.runtime.*
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.laba5.repository.AppRepository
import com.example.laba5.screens.*

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(navController, startDestination = "onboarding") {

        composable("onboarding") {
            OnboardingScreen(navController)
        }

        composable("name_input") {
            NameInputScreen(navController)
        }

        composable(
            "main/{name}",
            arguments = listOf(navArgument("name") { type = NavType.StringType })
        ) {
            val name = it.arguments?.getString("name") ?: ""
            MainScreen(name,navController)
        }

        composable(
            "detail/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) {
            val id = it.arguments?.getInt("id") ?: 0

            DetailScreen(id)
        }
    }
}