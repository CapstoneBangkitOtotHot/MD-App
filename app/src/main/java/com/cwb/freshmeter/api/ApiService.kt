package com.cwb.freshmeter.api

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): ApiResponse

    @POST("refresh-token")
    suspend fun refreshToken(@Body refreshTokenRequest: RefreshTokenRequest): ApiResponse

    @POST("register")
    fun registerUser(@Body request: RegisterRequest): Call<RegisterResponse>
}

data class LoginRequest(val email: String, val password: String)
data class RefreshTokenRequest(val refresh_token: String)
data class ApiResponse(val status: String, val data: TokenData)
data class TokenData(val session_token: String, val refresh_token: String)

data class RegisterResponse(
    val status: String,
    val message: String
)
data class RegisterRequest(
    val email: String,
    val password: String
)