package com.cwb.freshmeter.ui.login

import android.content.Context
import okhttp3.Interceptor
import okhttp3.Response

class AuthInterceptor(private val context: Context) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val sharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)
        val sessionToken = sharedPreferences.getString("session_token", null)

        val requestBuilder = chain.request().newBuilder()
        if (sessionToken != null) {
            requestBuilder.addHeader("Authorization", "Bearer $sessionToken")
        }

        return chain.proceed(requestBuilder.build())
    }
}