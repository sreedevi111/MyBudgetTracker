package com.example.myexpensetracker.service

/**
 * Extended budget data for display in budget card
 */
data class BudgetCardData(
    val id: Int,
    val name: String,
    val currency: String,
    val spentThisMonth: Double,
    val remaining: Double,
    val limit: Double,
    val isShared: Boolean = false
) {
    val spentPercentage: Float
        get() = if (limit > 0) (spentThisMonth / limit).toFloat().coerceIn(0f, 1f) else 0f
}

