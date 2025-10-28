package com.example.myexpensetracker.viewmodel

import android.app.Activity
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import androidx.credentials.CustomCredential
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.myexpensetracker.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import kotlinx.coroutines.launch
import androidx.lifecycle.viewModelScope
import com.example.myexpensetracker.service.ApiService
import com.example.myexpensetracker.service.GoogleAuthRequest
import com.example.myexpensetracker.service.TokenManager
import com.example.myexpensetracker.ui.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AuthViewModel(
    private val apiService: ApiService,
    private val tokenManager: TokenManager
) : ViewModel() {
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun startGoogleSignIn(
        activity: Activity,
        navController: NavController,
        credentialManager: CredentialManager
    ) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                val googleIdOption = GetGoogleIdOption.Builder()
                    .setServerClientId(activity.getString(R.string.server_client_id))
                    .setFilterByAuthorizedAccounts(false)
                    .setAutoSelectEnabled(false)
                    .build()

                val request = GetCredentialRequest.Builder()
                    .addCredentialOption(googleIdOption)
                    .build()

                val result = credentialManager.getCredential(
                    context = activity,
                    request = request
                )
                val cred = result.credential

                if (cred is CustomCredential &&
                    cred.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleTokenCred = GoogleIdTokenCredential.createFrom(cred.data)
                    val idToken = googleTokenCred.idToken
                    Log.d("ID Token", "startGoogleSignIn: $idToken")

                    // API call - this is where the loader is shown
                    val tokenPair = apiService.googleLogin(GoogleAuthRequest(idToken))
                    Log.d("Auth Success", "Access Token: ${tokenPair.access_token}")

                    // Save tokens
                    tokenManager.saveTokens(tokenPair.access_token, tokenPair.refresh_token)

                    _isLoading.value = false
                    navController.navigate(Screen.Profile.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            } catch (e: GetCredentialException) {
                Log.d("SignIn Error", "startGoogleSignIn: $e")
                _isLoading.value = false
            } catch (e: Exception) {
                Log.e("SignIn Error", "Unexpected error: $e")
                _isLoading.value = false
            }
        }
    }
}
