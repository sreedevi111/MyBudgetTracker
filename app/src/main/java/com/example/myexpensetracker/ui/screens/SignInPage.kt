package com.example.myexpensetracker.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myexpensetracker.ui.components.CustomButton
import com.example.myexpensetracker.ui.theme.MyExpenseTrackerTheme

@Composable
fun SignInScreen(
    isLoading: Boolean,
    onSignInClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Welcome to Budget Flow!",
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            Text(
                text = "Your collaborative mobile budget planner. Track expenses, manage categories, and share with ease.",
                fontSize = 14.sp,
                lineHeight = 20.sp
            )
            Text(
                text = "Please sign in to continue your financial journey.",
                fontSize = 14.sp,
                lineHeight = 20.sp
            )

            Spacer(Modifier.height(28.dp))

            GoogleSignInButton(
                enabled = !isLoading,
                onClick = onSignInClick
            )

            if (isLoading) {
                Spacer(Modifier.height(20.dp))
                CircularProgressIndicator(strokeWidth = 3.dp)
            }
        }
    }
}

@Composable
private fun GoogleSignInButton(
    enabled: Boolean,
    onClick: () -> Unit
) {

    CustomButton("Sign in with Google", onClick = onClick, borderRadius = 50, fontSize = 14)
}

@Preview(showBackground = true)
@Composable
private fun SignInPreview() {
    MyExpenseTrackerTheme {
        SignInScreen(
            isLoading = false,
            onSignInClick = {}
        )

    }
}
