package com.cwb.freshmeter.ui.login

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.cwb.freshmeter.api.LoginRequest
import com.cwb.freshmeter.api.RefreshTokenRequest
import com.cwb.freshmeter.api.RetrofitClient
import com.cwb.freshmeter.api.TokenData
import com.cwb.freshmeter.ui.main.MainActivity
import com.cwb.freshmeter.ui.profile.ProfileActivity
import kotlinx.coroutines.Dispatchers
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
        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()

        val intent = Intent(context, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    fun logout(context: Context) {
        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        with(sharedPreferences.edit()) {
            remove("session_token")
            remove("refresh_token")
            remove("user_email")
            apply()
        }

        val intent = Intent(context, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        context.startActivity(intent)
    }

    suspend fun refreshSessionToken(context: Context) {
        try {
            val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
            val refreshToken = sharedPreferences.getString("refresh_token", null)

            if (refreshToken != null) {
                val response = RetrofitClient.apiService.refreshToken(RefreshTokenRequest(refreshToken))
                if (response.status == "ok") {
                    withContext(Dispatchers.Main) {
                        with(sharedPreferences.edit()) {
                            putString("session_token", response.data.session_token)
                            apply()
                        }
                        Toast.makeText(context, "Session token refreshed", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Failed to refresh session token", Toast.LENGTH_SHORT).show()
                    }
                }
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "No refresh token found", Toast.LENGTH_SHORT).show()
                }
            }
        } catch (e: IOException) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Network error: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "An unexpected error occurred", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
