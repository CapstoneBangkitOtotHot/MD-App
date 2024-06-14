package com.cwb.freshmeter.ui.analyze

import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.cwb.freshmeter.R
import com.cwb.freshmeter.databinding.ActivityAnalyzeBinding

@Suppress("DEPRECATION")
class AnalyzeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalyzeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Get the image URI from the intent and display it
        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
        if (imageUri != null) {
            binding.ivResult.setImageURI(imageUri)
        }
    }

    @Deprecated("This method has been deprecated...")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
