package com.cwb.freshmeter.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse

    @POST("auth/refresh")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): ApiResponse

    @POST("auth/register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>

    @POST("auth/send-password-reset-email")
    suspend fun forgotPassword(@Body request: ForgotPasswordRequest): ForgotPasswordResponse

    @POST("auth/logout")
    suspend fun logout(): LogoutResponse
}

data class LoginRequest(val email: String, val password: String)
data class RefreshTokenRequest(val token: String)
data class ApiResponse(val status: String, val data: TokenData)
data class TokenData(val session_token: String, val refresh_token: String)
data class RegisterResponse(val status: String, val message: String)
data class RegisterRequest(val email: String, val password: String)
data class ForgotPasswordRequest(val email: String)
data class ForgotPasswordResponse(val status: String, val message: String)
data class LogoutResponse(val status: String)