package com.example.myexpensetracker.service

import retrofit2.http.*

interface ApiService {

    // ---------- AUTH ----------
    @POST("v1/auth/google")
    suspend fun googleLogin(@Body request: GoogleAuthRequest): TokenPair

    @POST("v1/auth/refresh")
    suspend fun refreshToken(@Body payload: Map<String, Any>): AccessToken

    // ---------- USER ----------
    @GET("v1/users/me")
    suspend fun getMe(): UserOut

    @POST("v1/users/logout")
    suspend fun logout(): Unit

    // ---------- BUDGETS ----------
    @GET("v1/budgets")
    suspend fun listBudgets(): List<BudgetOut>

    @POST("v1/budgets")
    suspend fun createBudget(@Body budget: BudgetCreate): BudgetOut

    @GET("v1/budgets/{budget_id}")
    suspend fun getBudget(@Path("budget_id") id: Int): BudgetOut

    @PATCH("v1/budgets/{budget_id}")
    suspend fun updateBudget(
        @Path("budget_id") id: Int,
        @Body update: BudgetUpdate
    ): BudgetOut

    @DELETE("v1/budgets/{budget_id}")
    suspend fun deleteBudget(@Path("budget_id") id: Int)

    // ---------- EXPENSES ----------
    @GET("v1/budgets/{budget_id}/expenses")
    suspend fun listExpenses(
        @Path("budget_id") budgetId: Int,
        @Query("page") page: Int = 1,
        @Query("page_size") size: Int = 50
    ): List<ExpenseOut>

    @POST("v1/budgets/{budget_id}/expenses")
    suspend fun createExpense(
        @Path("budget_id") budgetId: Int,
        @Body expense: ExpenseCreate
    ): ExpenseOut

    @PATCH("v1/expenses/{expense_id}")
    suspend fun updateExpense(
        @Path("expense_id") id: Int,
        @Body expense: ExpenseUpdate
    ): ExpenseOut

    @DELETE("v1/expenses/{expense_id}")
    suspend fun deleteExpense(@Path("expense_id") id: Int)
}

// ---------- DATA CLASSES ----------
data class GoogleAuthRequest(val id_token: String)
data class TokenPair(val access_token: String, val refresh_token: String)
data class AccessToken(val access_token: String)
data class UserOut(val id: Int, val email: String, val name: String?, val avatar_url: String?)
data class BudgetOut(val id: Int, val name: String, val currency: String, val month_start_day: Int)
data class BudgetCreate(val name: String, val currency: String, val month_start_day: Int = 1)
data class BudgetUpdate(val name: String?, val currency: String?, val month_start_day: Int?)
data class ExpenseOut(
    val id: Int,
    val budget_id: Int,
    val amount_cents: Int,
    val currency: String,
    val spent_at: String,
    val merchant: String?,
    val note: String?
)
data class ExpenseCreate(
    val amount_cents: Int,
    val currency: String,
    val spent_at: String,
    val merchant: String? = null,
    val note: String? = null
)
data class ExpenseUpdate(
    val amount_cents: Int?,
    val spent_at: String?,
    val merchant: String?,
    val note: String?
)
