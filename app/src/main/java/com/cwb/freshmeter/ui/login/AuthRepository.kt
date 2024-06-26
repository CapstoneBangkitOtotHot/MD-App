package com.cwb.freshmeter.ui.login

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import com.auth0.android.jwt.JWT
import com.cwb.freshmeter.api.LoginRequest
import com.cwb.freshmeter.api.RefreshTokenRequest
import com.cwb.freshmeter.api.RetrofitClient
import com.cwb.freshmeter.api.TokenData
import com.cwb.freshmeter.ui.main.MainActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

object AuthRepository {

    suspend fun login(context: Context, email: String, password: String): Boolean {
        return try {
            val response = RetrofitClient.apiService.login(LoginRequest(email, password))
            if (response.status == "ok") {
                withContext(Dispatchers.Main) {
                    handleLoginSuccess(context, response.data, email)
                }
                true
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
                false
            }
        } catch (e: HttpException) {
            withContext(Dispatchers.Main) {
                when (e.code()) {
                    400 -> Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    else -> Toast.makeText(context, "Server error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            false
        } catch (e: IOException) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            false
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "An unexpected error occurred", Toast.LENGTH_SHORT).show()
            }
            false
        }
    }

    private fun handleLoginSuccess(context: Context, data: TokenData, email: String) {
        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            putString("session_token", data.session_token)
            putString("refresh_token", data.refresh_token)
            putString("user_email", email)
            apply()
        }

        // rebuild client
        RetrofitClient.rebuild_client(data.session_token)

        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    fun logout(context: Context) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.apiService.logout()
                if (response.status == "ok") {
                    withContext(Dispatchers.Main) {
                        clearSession(context)
                        navigateToLogin(context)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Logout failed", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clearSession(context: Context) {
        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("session_token")
            remove("refresh_token")
            remove("user_email")
            apply()
        }
    }

    private fun navigateToLogin(context: Context) {
        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    suspend fun refreshSessionToken(context: Context) {
        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val refreshToken = sharedPreferences.getString("refresh_token", null)

        if (refreshToken != null) {
            try {
                val refreshTokenRequest = RefreshTokenRequest(refreshToken)
                Log.d("AuthRepository", "Refreshing token with request: $refreshTokenRequest")

                val response = RetrofitClient.apiService.refreshToken(refreshTokenRequest)
                if (response.status == "ok") {
                    with(sharedPreferences.edit()) {
                        putString("session_token", response.data.session_token)
                        apply()
                    }
                    // rebuild client
                    RetrofitClient.rebuild_client(response.data.session_token)

                    Log.d("AuthRepository", "Session token refreshed successfully")
                } else {
                    Log.d("AuthRepository", "Failed to refresh session token: ${response.status}")
                }
            } catch (e: HttpException) {
                Log.e("AuthRepository", "HTTP error during token refresh: ${e.code()}", e)
            } catch (e: IOException) {
                Log.e("AuthRepository", "Network error during token refresh", e)
            } catch (e: Exception) {
                Log.e("AuthRepository", "Unexpected error during token refresh", e)
            }
        } else {
            Log.d("AuthRepository", "No refresh token found")
        }
    }

    fun isTokenExpired(token: String): Boolean {
        return try {
            val jwt = JWT(token)
            val isExpired = jwt.isExpired(10) // 10 seconds leeway to account for clock skew
            isExpired
        } catch (e: Exception) {
            true // Treat any decoding error as expired token
        }
    }
}
