package com.example.myexpensetracker

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.credentials.CredentialManager
import androidx.navigation.compose.rememberNavController
import com.example.myexpensetracker.service.ApiClient
import com.example.myexpensetracker.ui.navigation.AppNavGraph
import com.example.myexpensetracker.ui.theme.MyExpenseTrackerTheme
import com.example.myexpensetracker.viewmodel.AuthViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MyExpenseTrackerTheme {
                val navController = rememberNavController()
                val credentialManager = remember { CredentialManager.create(this@MainActivity) }
                val apiService = remember { ApiClient.getService(this@MainActivity) }

                // Manual factory for ViewModel without Hilt
                val authViewModel = remember { AuthViewModel(apiService) }

                AppNavGraph(
                    navController = navController,
                    authViewModel = authViewModel,
                    credentialManager = credentialManager,
                    activity = this@MainActivity
                )
            }
        }
    }
}
