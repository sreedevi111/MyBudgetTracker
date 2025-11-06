package com.example.myexpensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myexpensetracker.service.BudgetCardData
import com.example.myexpensetracker.ui.components.BudgetCard
import com.example.myexpensetracker.ui.components.CustomHeader
import com.example.myexpensetracker.ui.theme.BackgroundWhite
import com.example.myexpensetracker.ui.theme.PrimaryBlue
import com.example.myexpensetracker.ui.theme.TextGrayLight
import com.example.myexpensetracker.viewmodel.BudgetViewModel

/**
 * Stateful BudgetPage that connects to the ViewModel
 */
@Composable
fun BudgetPage(
    viewModel: BudgetViewModel,
    onCreateBudget: () -> Unit = {},
    onAuthError: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val budgets by viewModel.budgets.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val authError by viewModel.authenticationError.collectAsState()

    // Fetch budgets when the screen loads
    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.refresh()
    }

    // Handle authentication errors
    androidx.compose.runtime.LaunchedEffect(authError) {
        if (authError) {
            viewModel.clearAuthError()
            onAuthError()
        }
    }

    BudgetPageContent(
        budgets = budgets,
        isLoading = isLoading,
        error = error,
        onBudgetClick = { budget ->
            // TODO: Navigate to budget detail
        },
        onRefresh = { viewModel.refresh() },
        onCreateBudget = onCreateBudget,
        modifier = modifier
    )
}

/**
 * Stateless BudgetPage content
 */
@Composable
fun BudgetPageContent(
    modifier: Modifier = Modifier,
    budgets: List<BudgetCardData>,
    isLoading: Boolean = false,
    error: String? = null,
    onBudgetClick: (BudgetCardData) -> Unit = {},
    onRefresh: () -> Unit = {},
    onCreateBudget: () -> Unit = {},
    ) {
    Scaffold(
        containerColor = BackgroundWhite,
        floatingActionButton = {
            FloatingActionButton(
                onClick = onCreateBudget,
                containerColor = PrimaryBlue,
                contentColor = androidx.compose.ui.graphics.Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "New Budget",
                    modifier = Modifier.padding(4.dp)
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(BackgroundWhite)
                .padding(horizontal = 16.dp)
                .padding(paddingValues)
        ) {
            // Header
            CustomHeader(
                title = "Budgets",
                onBackClick = null,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Loading state
            if (isLoading && budgets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = PrimaryBlue
                    )
                }
            }
            // Error state
            else if (error != null && budgets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = TextGrayLight
                        )
                    }
                }
            }
            // Empty state
            else if (budgets.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No budgets yet",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextGrayLight
                    )
                }
            }
            // Budget List
            else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(budgets) { budget ->
                        BudgetCard(
                            budgetData = budget,
                            onClick = { onBudgetClick(budget) }
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun BudgetPagePreview() {
    val sampleBudgets = listOf(
        BudgetCardData(
            id = 1,
            name = "Household Expenses",
            currency = "EUR",
            spentThisMonth = 450.75,
            remaining = 349.25,
            limit = 800.0,
            isShared = true
        ),
        BudgetCardData(
            id = 2,
            name = "Personal Budget",
            currency = "USD",
            spentThisMonth = 1250.50,
            remaining = 749.50,
            limit = 2000.0,
            isShared = false
        ),
        BudgetCardData(
            id = 3,
            name = "Vacation Fund",
            currency = "EUR",
            spentThisMonth = 200.0,
            remaining = 800.0,
            limit = 1000.0,
            isShared = false
        )
    )
    BudgetPageContent(budgets = sampleBudgets)
}

