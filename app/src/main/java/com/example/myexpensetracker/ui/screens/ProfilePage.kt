package com.example.myexpensetracker.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

import com.example.myexpensetracker.R
import com.example.myexpensetracker.ui.components.CustomButton
import com.example.myexpensetracker.ui.components.CustomHeader

@Composable
fun ProfilePage(
    onSignOut: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        // Header
        CustomHeader(
            title = "Profile",
            onBackClick = null,
            modifier = Modifier.padding(top = 36.dp, bottom = 32.dp)
        )

        // Profile Content
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            // Profile Image with Status Indicator
            Box(
                modifier = Modifier.size(120.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
//                AsyncImage(
//                    model = "https://via.placeholder.com/120", // Replace with actual image URL
//                    contentDescription = "Profile Picture",
//                    modifier = Modifier
//                        .size(120.dp)
//                        .clip(CircleShape)
//                        .border(3.dp, Color(0xFFE0E0E0), CircleShape),
//                    contentScale = ContentScale.Crop
//                )


            }

            // User Name
            Text(
                text = "Alice Johnson",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 20.dp)
            )

            // Email
            Text(
                text = "alice.johnson@example.com",
                fontSize = 14.sp,
                color = Color(0xFF666666),
                modifier = Modifier.padding(top = 8.dp)
            )
            // Sign Out Button
            CustomButton(
                text = "Sign Out",
                onClick = onSignOut,
                backgroundColor = Color(0xFFE74C3C),
                borderRadius = 20,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 24.dp)
            )
        }


    }
}

@Composable
fun AsyncImage(
    model: String,
    contentDescription: String,
    modifier: Modifier,
    contentScale: ContentScale
) {
    TODO("Not yet implemented")
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun ProfilePagePreview() {
    ProfilePage()
}
