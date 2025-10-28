package com.example.myexpensetracker.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("SgnIn")
    object Profile : Screen("Profile")
}