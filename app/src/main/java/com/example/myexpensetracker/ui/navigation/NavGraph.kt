package com.example.myexpensetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.credentials.CredentialManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myexpensetracker.service.ApiClient
import com.example.myexpensetracker.service.TokenManager
import com.example.myexpensetracker.ui.screens.SignInScreen
import com.example.myexpensetracker.ui.screens.MainScreen
import com.example.myexpensetracker.viewmodel.AuthViewModel
import com.example.myexpensetracker.viewmodel.ProfileViewModel
import com.example.myexpensetracker.viewmodel.BudgetViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    credentialManager: CredentialManager,
    activity: androidx.activity.ComponentActivity
) {
    val isLoading by authViewModel.isLoading.collectAsState()

    // Create ViewModels
    val apiService = remember { ApiClient.getService(activity) }
    val tokenManager = remember { TokenManager(activity) }
    val profileViewModel = remember { ProfileViewModel(apiService, tokenManager) }
    val budgetViewModel = remember { BudgetViewModel(apiService) }

    NavHost(
        navController = navController,
        startDestination = Screen.Login.route
    ) {
        composable(Screen.Login.route) {
            SignInScreen(
                isLoading = isLoading,
                onSignInClick = {
                    authViewModel.startGoogleSignIn(activity, navController, credentialManager)
                }
            )
        }

        composable(Screen.Main.route) {
            MainScreen(
                profileViewModel = profileViewModel,
                budgetViewModel = budgetViewModel,
                onSignOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Main.route) { inclusive = true }
                    }
                },
                onCreateBudget = {
                    // TODO: Navigate to create budget screen
                }
            )
        }
    }
}
