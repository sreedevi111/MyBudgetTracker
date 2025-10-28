package com.example.myexpensetracker.service

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import kotlinx.coroutines.runBlocking
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object ApiClient {

    private const val BASE_URL = "https://budget-flow-dev-9dcfff7dd9ee.herokuapp.com/"

    private const val PREFS_NAME = "auth_prefs"
    private const val ACCESS = "access_token"
    private const val REFRESH = "refresh_token"

    private lateinit var apiService: ApiService

    fun getService(context: Context): ApiService {
        if (::apiService.isInitialized) return apiService

        val prefs = EncryptedSharedPreferences.create(
            context,
            PREFS_NAME,
            MasterKey.Builder(context).setKeyScheme(MasterKey.KeyScheme.AES256_GCM).build(),
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )

        val authInterceptor = Interceptor { chain ->
            val token = prefs.getString(ACCESS, null)
            val request = chain.request().newBuilder()
            if (!token.isNullOrEmpty()) {
                request.addHeader("Authorization", "Bearer $token")
            }
            chain.proceed(request.build())
        }

        val authenticator = Authenticator { _, response ->
            val refresh = prefs.getString(REFRESH, null) ?: return@Authenticator null
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
                prefs.edit().putString(ACCESS, newToken.access_token).apply()

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
