package com.cwb.freshmeter.api

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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

    @POST("auth/change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): ChangePasswordResponse

    @Multipart
    @POST("api/predict/fruit")
    fun infer(
        @Part image: MultipartBody.Part
    ): Call<InferenceResponse>
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
data class ChangePasswordRequest(val old_password: String, val new_password: String)
data class ChangePasswordResponse(val status: String, val message: String)

@Parcelize
data class InferenceData(
    val fruit_class: Int,
    val fruit_class_string: String,
    val cropped_img: String,
    val confidence: Float,
    val freshness_percentage: String,
    val freshness_days: Int,
    val tips: List<String>
) : Parcelable

@Parcelize
data class Inference(
    val orig_img: String,
    val inferences: List<InferenceData>
) : Parcelable

@Parcelize
data class InferenceResponse(
    val status: String,
    val data: Inference
) : Parcelable
