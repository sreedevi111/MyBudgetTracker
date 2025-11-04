package com.example.myexpensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myexpensetracker.ui.components.CurrencyDropdown
import com.example.myexpensetracker.ui.components.CustomButton
import com.example.myexpensetracker.ui.components.CustomHeader
import com.example.myexpensetracker.ui.components.CustomTextInput
import com.example.myexpensetracker.ui.theme.BackgroundWhite
import com.example.myexpensetracker.ui.theme.PrimaryBlue
import com.example.myexpensetracker.viewmodel.BudgetViewModel

@Composable
fun CreateBudgetPage(
    viewModel: BudgetViewModel,
    onBack: () -> Unit,
    onBudgetCreated: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    CreateBudgetContent(
        isLoading = isLoading,
        error = error,
        onBack = onBack,
        onSave = { name, currency, monthStartDay ->
            viewModel.createBudget(
                name = name,
                currency = currency,
                monthStartDay = monthStartDay,
                onSuccess = onBudgetCreated
            )
        },
        modifier = modifier
    )
}

@Composable
fun CreateBudgetContent(
    isLoading: Boolean = false,
    error: String? = null,
    onBack: () -> Unit = {},
    onSave: (name: String, currency: String, monthStartDay: Int) -> Unit = { _, _, _ -> },
    modifier: Modifier = Modifier
) {
    var budgetName by remember { mutableStateOf("") }
    var selectedCurrency by remember { mutableStateOf("USD") }
    var nameError by remember { mutableStateOf<String?>(null) }

    val scrollState = rememberScrollState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundWhite)
            .padding(horizontal = 16.dp)
    ) {
        // Header
        CustomHeader(
            title = "Create Budget",
            onBackClick = onBack,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        // Scrollable Content
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
        ) {
            // Budget Name Input
            CustomTextInput(
                value = budgetName,
                onValueChange = {
                    budgetName = it
                    nameError = null
                },
                label = "Budget Name",
                placeholder = "e.g., Monthly Essentials",
                isError = nameError != null,
                errorMessage = nameError,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 20.dp)
            )

            // Currency Dropdown
            CurrencyDropdown(
                selectedCurrency = selectedCurrency,
                onCurrencySelected = { selectedCurrency = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 32.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))
        }

        // Save Button
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    color = PrimaryBlue,
                    modifier = Modifier.size(48.dp)
                )
            } else {
                CustomButton(
                    text = "Save Budget",
                    onClick = {
                        // Validate
                        if (budgetName.isBlank()) {
                            nameError = "Budget name is required"
                            return@CustomButton
                        }

                        // Call save
                        onSave(budgetName.trim(), selectedCurrency, 1)
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(horizontal = 0.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun CreateBudgetPagePreview() {
    CreateBudgetContent()
}

