package com.example.myexpensetracker.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.credentials.CredentialManager
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.myexpensetracker.ui.screens.SignInScreen
import com.example.myexpensetracker.ui.screens.ProfilePage
import com.example.myexpensetracker.viewmodel.AuthViewModel

@Composable
fun AppNavGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    credentialManager: CredentialManager,
    activity: androidx.activity.ComponentActivity
) {
    val isLoading by authViewModel.isLoading.collectAsState()

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

        composable(Screen.Profile.route) {
            ProfilePage(
                onSignOut = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                }
            )
        }
    }
}
