package com.cwb.freshmeter.ui.login

import android.app.Application
import android.content.Context

class MyApp : Application() {

    init {
        instance = this
    }

    companion object {
        private var instance: MyApp? = null

        val context: Context
            get() = instance!!.applicationContext
    }
}