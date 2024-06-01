package com.cwb.freshmeter.ui.main

import android.os.Bundle
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.cwb.freshmeter.R

class WebViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_web_view)

        val webView = findViewById<WebView>(R.id.web_view)
        webView.settings.javaScriptEnabled = true // Enable JavaScript for some websites

        val url = intent.getStringExtra("articleUrl") ?: return // Handle potential null URL

        webView.loadUrl(url)

    }
}