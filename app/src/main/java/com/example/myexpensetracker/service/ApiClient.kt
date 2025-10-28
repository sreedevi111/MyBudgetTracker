package com.example.myexpensetracker.service

import android.content.Context
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "https://budget-flow-dev-9dcfff7dd9ee.herokuapp.com/"

    private lateinit var apiService: ApiService
    private lateinit var tokenManager: TokenManager

    fun getService(context: Context): ApiService {
        if (::apiService.isInitialized) return apiService

        tokenManager = TokenManager(context)

        val authInterceptor = Interceptor { chain ->
            val token = tokenManager.getAccessToken()
            val request = chain.request().newBuilder()
            if (!token.isNullOrEmpty()) {
                request.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(request.build())
        }

        val authenticator = Authenticator { _, response ->
            val refresh = tokenManager.getRefreshToken() ?: return@Authenticator null
            if (responseCount(response) >= 2) return@Authenticator null

            return@Authenticator try {
                val newToken = runBlocking {
                    val retrofit = Retrofit.Builder()
                        .baseUrl(BASE_URL)
                        .addConverterFactory(GsonConverterFactory.create())
                        .build()
                    val authApi = retrofit.create(ApiService::class.java)
                    authApi.refreshToken(mapOf("refresh_token" to refresh))
                }
                tokenManager.saveTokens(newToken.access_token, refresh)

                response.request.newBuilder()
                    .header("Authorization", "Bearer ${newToken.access_token}")
                    .build()
            } catch (_: Exception) {
                null
            }
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .authenticator(authenticator)
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .build()

        apiService = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)

        return apiService
    }

    private fun responseCount(response: Response): Int {
        var count = 1
        var r = response.priorResponse
        while (r != null) {
            count++
            r = r.priorResponse
        }
        return count
    }
}
