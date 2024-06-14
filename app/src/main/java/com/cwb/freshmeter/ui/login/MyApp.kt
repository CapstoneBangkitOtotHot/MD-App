package com.cwb.freshmeter.ui.login

import android.app.Application
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.cwb.freshmeter.worker.RefreshTokenWorker
import java.util.concurrent.TimeUnit

class MyApp : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MyApp? = null

        val context: Context
            get() = instance!!.applicationContext
    }

    override fun onCreate() {
        super.onCreate()
        setupPeriodicTokenRefresh()
    }

    private fun setupPeriodicTokenRefresh() {
        val refreshTokenRequest = PeriodicWorkRequestBuilder<RefreshTokenWorker>(15, TimeUnit.MINUTES)
            .build()

        WorkManager.getInstance(this)
            .enqueueUniquePeriodicWork(
                "RefreshTokenWork",
                ExistingPeriodicWorkPolicy.REPLACE,
                refreshTokenRequest
            )
    }
}