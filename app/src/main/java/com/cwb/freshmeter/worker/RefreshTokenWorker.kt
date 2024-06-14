package com.cwb.freshmeter.worker

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.cwb.freshmeter.ui.login.AuthRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RefreshTokenWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("RefreshTokenWorker", "Starting token refresh")
                val context = applicationContext
                AuthRepository.refreshSessionToken(context)
                Log.d("RefreshTokenWorker", "Token refreshed successfully")
                Result.success()
            } catch (e: Exception) {
                Log.e("RefreshTokenWorker", "Token refresh failed", e)
                Result.retry()
            }
        }
    }
}