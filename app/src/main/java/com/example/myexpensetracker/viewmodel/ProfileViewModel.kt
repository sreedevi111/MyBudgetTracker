package com.example.myexpensetracker.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myexpensetracker.service.ApiService
import com.example.myexpensetracker.service.TokenManager
import com.example.myexpensetracker.service.UserOut
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _userProfile = MutableStateFlow<UserOut?>(null)
    val userProfile: StateFlow<UserOut?> = _userProfile.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    fun fetchUserProfile() {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val user = apiService.getMe()
                _userProfile.value = user
                Log.d("ProfileViewModel", "User profile fetched: $user")
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error fetching user profile", e)
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun signOut(onSignOutComplete: () -> Unit) {
        viewModelScope.launch {
            try {
                apiService.logout()
            } catch (e: Exception) {
                Log.e("ProfileViewModel", "Error during logout", e)
            } finally {
                tokenManager.clearTokens()
                _userProfile.value = null
                onSignOutComplete()
            }
        }
    }
}

