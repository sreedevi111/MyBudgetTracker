package com.example.myexpensetracker.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.myexpensetracker.R
import com.example.myexpensetracker.ui.components.BottomNavTab
import com.example.myexpensetracker.ui.components.BottomNavigationBar
import com.example.myexpensetracker.ui.theme.BackgroundWhite
import com.example.myexpensetracker.viewmodel.BudgetViewModel
import com.example.myexpensetracker.viewmodel.ProfileViewModel

@Composable
fun MainScreen(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    budgetViewModel: BudgetViewModel,
    onSignOut: () -> Unit,
    onCreateBudget: () -> Unit = {},
) {
    var selectedTab by remember { mutableIntStateOf(1) } // Start with Profile tab (index 1)

    val tabs = listOf(
        BottomNavTab(icon = R.drawable.ic_list, label = "Budgets"),
        BottomNavTab(icon = R.drawable.ic_user, label = "Profile")
    )

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = BackgroundWhite,
        bottomBar = {
            BottomNavigationBar(
                tabs = tabs,
                selectedTab = selectedTab,
                onTabSelected = { selectedTab = it }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (selectedTab) {
                0 -> BudgetPage(
                    viewModel = budgetViewModel,
                    onCreateBudget = onCreateBudget,
                    onAuthError = onSignOut
                )
                1 -> ProfilePage(
                    profileViewModel = profileViewModel,
                    onSignOut = onSignOut
                )
            }
        }
    }
}

