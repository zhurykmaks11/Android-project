package com.example.laba5

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.example.laba5.navigation.NavGraph
import com.example.laba5.ui.theme.AppTheme
import com.example.laba5.viewmodel.ProfileViewModel
import com.example.laba5.viewmodel.UserViewModel

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {

            val navController = rememberNavController()

            val userViewModel: UserViewModel = viewModel()
            val profileViewModel: ProfileViewModel = viewModel()

            val isDarkTheme by profileViewModel.isDarkTheme.collectAsState()

            AppTheme(darkTheme = isDarkTheme) {
                NavGraph(navController, userViewModel)
            }
        }
    }
}