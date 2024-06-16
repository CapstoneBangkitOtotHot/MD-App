package com.cwb.freshmeter.api

import android.util.Log
import com.cwb.freshmeter.ui.login.AuthInterceptor
import com.cwb.freshmeter.ui.login.MyApp
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


public class RetrofitClient {
    companion object {

        var apiService: ApiService = rebuild_client("No Token")
        fun rebuild_client (token: String): ApiService {
            Log.wtf("WTF", token)
            val client = OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(ServiceInterceptor(token))
                .build()

            apiService = Retrofit.Builder()
                .client(client)
                .baseUrl("http://10.9.8.7:5000/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)

            return apiService
        }
    }

    class ServiceInterceptor(token: String?) : Interceptor {
        var token = token
        override fun intercept(chain: Interceptor.Chain): Response = chain.run {
            proceed(
                request()
                    .newBuilder()
                    .addHeader("Authorization", "Bearer " + token)
                    .build()
            )
        }

    }
}