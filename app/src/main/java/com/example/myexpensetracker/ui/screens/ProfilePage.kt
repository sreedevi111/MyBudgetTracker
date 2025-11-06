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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

import com.example.myexpensetracker.ui.components.CustomButton
import com.example.myexpensetracker.ui.components.CustomHeader
import com.example.myexpensetracker.viewmodel.ProfileViewModel

@Composable
fun ProfilePage(
    modifier: Modifier = Modifier,
    profileViewModel: ProfileViewModel,
    onSignOut: () -> Unit = {}
) {
    // Fetch user profile when the screen loads
    LaunchedEffect(Unit) {
        profileViewModel.fetchUserProfile()
    }

    val userProfile by profileViewModel.userProfile.collectAsState()
    val isLoading by profileViewModel.isLoading.collectAsState()
    val error by profileViewModel.error.collectAsState()
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

        // Show loading indicator
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Color(0xFF6200EE))
            }
            return@Column
        }

        // Show error message
        if (error != null) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: $error",
                    color = Color.Red,
                    fontSize = 16.sp
                )
            }
            return@Column
        }

        // Profile Content
        userProfile?.let { user ->
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
                    // Display user avatar from API or fallback to placeholder
                    if (!user.avatar_url.isNullOrEmpty()) {
                        AsyncImage(
                            model = user.avatar_url,
                            contentDescription = "Profile Picture",
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color(0xFFE0E0E0), CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        // Fallback to text initial
                        Box(
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .border(3.dp, Color(0xFFE0E0E0), CircleShape)
                                .background(Color(0xFF6200EE)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = user.name?.firstOrNull()?.uppercase() ?: user.email.firstOrNull()?.uppercase() ?: "?",
                                fontSize = 48.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                }

                // User Name
                Text(
                    text = user.name ?: "No name",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 20.dp)
                )

                // Email
                Text(
                    text = user.email,
                    fontSize = 14.sp,
                    color = Color(0xFF666666),
                    modifier = Modifier.padding(top = 8.dp)
                )

                // Sign Out Button
                CustomButton(
                    text = "Sign Out",
                    onClick = {
                        profileViewModel.signOut(onSignOut)
                    },
                    backgroundColor = Color(0xFFE74C3C),
                    borderRadius = 20,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp)
                )
            }
        }

    }
}

