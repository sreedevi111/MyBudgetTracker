package com.example.myexpensetracker.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myexpensetracker.service.BudgetCardData
import com.example.myexpensetracker.ui.theme.PrimaryBlue
import com.example.myexpensetracker.ui.theme.TextGrayLight
import java.util.*

@Composable
fun BudgetCard(
    modifier: Modifier = Modifier,
    budgetData: BudgetCardData,
    onClick: () -> Unit = {},
    ) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        ),
        onClick = onClick
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Header row with title and shared badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = budgetData.name,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                if (budgetData.isShared) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFF6BBF59),
                                shape = RoundedCornerShape(20.dp)
                            )
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "Shared",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Spent this month
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Spent this month",
                    fontSize = 14.sp,
                    color = TextGrayLight
                )

                Text(
                    text = formatCurrency(budgetData.spentThisMonth, budgetData.currency),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Remaining amount
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Remaining",
                    fontSize = 14.sp,
                    color = TextGrayLight
                )

                Text(
                    text = formatCurrency(budgetData.remaining, budgetData.currency),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Progress bar
            LinearProgressIndicator(
                progress = { budgetData.spentPercentage },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(8.dp),
                color = PrimaryBlue,
                trackColor = Color(0xFFE8E8E8),
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Bottom row with remaining and limit
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Remaining ${
                        formatCurrency(
                            budgetData.remaining,
                            budgetData.currency,
                            includeSymbol = false
                        )
                    }",
                    fontSize = 13.sp,
                    color = PrimaryBlue,
                    fontWeight = FontWeight.Medium
                )

                Text(
                    text = "Limit: ${formatCurrency(budgetData.limit, budgetData.currency)}",
                    fontSize = 13.sp,
                    color = TextGrayLight
                )
            }
        }
    }
}

/**
 * Helper function to format currency with proper symbol and decimal places
 */
private fun formatCurrency(
    amount: Double,
    currencyCode: String,
    includeSymbol: Boolean = true
): String {
    val currencySymbol = when (currencyCode.uppercase()) {
        "EUR" -> "€"
        "USD" -> "$"
        "GBP" -> "£"
        "INR" -> "₹"
        else -> currencyCode
    }

    val formattedAmount = String.format(Locale.US, "%.2f", amount)

    return if (includeSymbol) {
        when (currencyCode.uppercase()) {
            "EUR", "GBP" -> "$currencySymbol $formattedAmount"
            else -> "$currencySymbol$formattedAmount"
        }
    } else {
        formattedAmount
    }
}

@Preview(showBackground = true)
@Composable
fun BudgetCardPreview() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5))
            .padding(16.dp)
    ) {
        BudgetCard(
            budgetData = BudgetCardData(
                id = 1,
                name = "Household Expenses",
                currency = "EUR",
                spentThisMonth = 450.75,
                remaining = 349.25,
                limit = 800.0,
                isShared = true
            )
        )

        Spacer(modifier = Modifier.height(16.dp))

        BudgetCard(
            budgetData = BudgetCardData(
                id = 2,
                name = "Personal Budget",
                currency = "USD",
                spentThisMonth = 1250.50,
                remaining = 749.50,
                limit = 2000.0,
                isShared = false
            )
        )
    }
}
