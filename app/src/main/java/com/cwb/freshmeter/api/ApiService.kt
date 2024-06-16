package com.cwb.freshmeter.api

import android.os.Parcel
import android.os.Parcelable
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

// untuk ml
data class InferenceData(
    val fruit_class: Int,
    val cropped_img: String,
    val confidence: Float,
    val freshness: Float?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readFloat()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(fruit_class)
        parcel.writeString(cropped_img)
        parcel.writeFloat(confidence)
        parcel.writeValue(freshness)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InferenceData> {
        override fun createFromParcel(parcel: Parcel): InferenceData {
            return InferenceData(parcel)
        }

        override fun newArray(size: Int): Array<InferenceData?> {
            return arrayOfNulls(size)
        }
    }
}


data class Inference(
    val orig_img: String,
    val inferences: ArrayList<InferenceData> // Use ArrayList instead of List
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        ArrayList<InferenceData>().apply {
            parcel.readTypedList(this, InferenceData.CREATOR)
        }
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(orig_img)
        parcel.writeTypedList(inferences) // Use writeTypedList instead of writeList
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Inference> {
        override fun createFromParcel(parcel: Parcel): Inference {
            return Inference(parcel)
        }

        override fun newArray(size: Int): Array<Inference?> {
            return arrayOfNulls(size)
        }
    }
}

data class InferenceResponse(
    val status: String,
    val data: Inference
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readParcelable<Inference>(Inference::class.java.classLoader) ?: Inference("", ArrayList())
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(status)
        parcel.writeParcelable(data, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<InferenceResponse> {
        override fun createFromParcel(parcel: Parcel): InferenceResponse {
            return InferenceResponse(parcel)
        }

        override fun newArray(size: Int): Array<InferenceResponse?> {
            return arrayOfNulls(size)
        }
    }
}
