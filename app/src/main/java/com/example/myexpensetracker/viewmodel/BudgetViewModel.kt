package com.example.myexpensetracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myexpensetracker.service.ApiService
import com.example.myexpensetracker.service.BudgetCardData
import com.example.myexpensetracker.service.BudgetOut
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * ViewModel for managing budgets
 */
class BudgetViewModel(
    private val apiService: ApiService
) : ViewModel() {

    private val _budgets = MutableStateFlow<List<BudgetCardData>>(emptyList())
    val budgets: StateFlow<List<BudgetCardData>> = _budgets.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _authenticationError = MutableStateFlow(false)
    val authenticationError: StateFlow<Boolean> = _authenticationError.asStateFlow()

    init {
        loadBudgets()
    }

    fun clearAuthError() {
        _authenticationError.value = false
    }

    /**
     * Load budgets from API
     */
    fun loadBudgets() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                Log.d("BudgetViewModel", "Starting to load budgets")
                val budgetsResponse = apiService.listBudgets()
                Log.d("BudgetViewModel", "Successfully loaded ${budgetsResponse.size} budgets")

                // TODO: For now, we'll create sample data
                // In the future, you'll need to fetch expense data and calculate spent/remaining
                _budgets.value = budgetsResponse.map { budget ->
                    convertToBudgetCardData(budget)
                }

            } catch (e: retrofit2.HttpException) {
                Log.e("BudgetViewModel", "HTTP error loading budgets: ${e.code()}", e)
                when (e.code()) {
                    401 -> {
                        _error.value = "Authentication failed. Please sign in again."
                        _authenticationError.value = true
                    }
                    403 -> _error.value = "Access denied"
                    404 -> _error.value = "Budgets not found"
                    500 -> _error.value = "Server error. Please try again later."
                    else -> _error.value = "Failed to load budgets: ${e.message()}"
                }
            } catch (e: java.net.UnknownHostException) {
                Log.e("BudgetViewModel", "Network error loading budgets", e)
                _error.value = "No internet connection"
            } catch (e: java.net.SocketTimeoutException) {
                Log.e("BudgetViewModel", "Timeout loading budgets", e)
                _error.value = "Request timed out. Please try again."
            } catch (e: Exception) {
                Log.e("BudgetViewModel", "Error loading budgets", e)
                _error.value = e.message ?: "Failed to load budgets"
            } finally {
                _isLoading.value = false
            }
        }
    }

    /**
     * Convert BudgetOut to BudgetCardData
     * TODO: This is a placeholder. You'll need to:
     * 1. Fetch expenses for the current month
     * 2. Calculate total spent
     * 3. Get budget limit (might need to be added to API)
     * 4. Calculate remaining
     */
    private fun convertToBudgetCardData(budget: BudgetOut): BudgetCardData {
        // Placeholder values - replace with actual data from expenses API
        val limit = 1000.0 // TODO: Get from budget settings
        val spentThisMonth = 0.0 // TODO: Calculate from expenses
        val remaining = limit - spentThisMonth

        return BudgetCardData(
            id = budget.id,
            name = budget.name,
            currency = budget.currency,
            spentThisMonth = spentThisMonth,
            remaining = remaining,
            limit = limit,
            isShared = false // TODO: Get from budget settings if API supports it
        )
    }

    /**
     * Refresh budgets
     */
    fun refresh() {
        loadBudgets()
    }

    /**
     * Create a new budget
     */
    fun createBudget(
        name: String,
        currency: String,
        monthStartDay: Int = 1,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null

                Log.d("BudgetViewModel", "Creating budget: $name")
                val budgetCreate = com.example.myexpensetracker.service.BudgetCreate(
                    name = name,
                    currency = currency,
                    month_start_day = monthStartDay
                )

                apiService.createBudget(budgetCreate)
                Log.d("BudgetViewModel", "Budget created successfully")

                // Reload budgets after creation
                loadBudgets()

                // Call success callback
                onSuccess()

            } catch (e: retrofit2.HttpException) {
                Log.e("BudgetViewModel", "HTTP error creating budget: ${e.code()}", e)
                when (e.code()) {
                    401 -> {
                        _error.value = "Authentication failed. Please sign in again."
                        _authenticationError.value = true
                    }
                    403 -> _error.value = "Access denied"
                    400 -> _error.value = "Invalid budget data"
                    500 -> _error.value = "Server error. Please try again later."
                    else -> _error.value = "Failed to create budget: ${e.message()}"
                }
            } catch (e: java.net.UnknownHostException) {
                Log.e("BudgetViewModel", "Network error creating budget", e)
                _error.value = "No internet connection"
            } catch (e: java.net.SocketTimeoutException) {
                Log.e("BudgetViewModel", "Timeout creating budget", e)
                _error.value = "Request timed out. Please try again."
            } catch (e: Exception) {
                Log.e("BudgetViewModel", "Error creating budget", e)
                _error.value = e.message ?: "Failed to create budget"
            } finally {
                _isLoading.value = false
            }
        }
    }
}

