package com.cwb.freshmeter.ui.analyze

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cwb.freshmeter.R
import com.cwb.freshmeter.api.InferenceResponse
import com.cwb.freshmeter.databinding.ActivityAnalyzeBinding

@Suppress("DEPRECATION")
class AnalyzeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalyzeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inferenceResponse = intent.getParcelableExtra<InferenceResponse>("data")
        if (inferenceResponse != null) {
            val data = inferenceResponse.data
            val inferences = data.inferences

            binding.rvInferences.setHasFixedSize(true)
            binding.rvInferences.layoutManager = LinearLayoutManager(this)

            val adapter = AnalyzeAdapter(this, inferences)
            binding.rvInferences.adapter = adapter
        } else {
            Toast.makeText(this, "No inference response found", Toast.LENGTH_SHORT).show()
        }
    }

    @Deprecated("This method has been deprecated...")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}


