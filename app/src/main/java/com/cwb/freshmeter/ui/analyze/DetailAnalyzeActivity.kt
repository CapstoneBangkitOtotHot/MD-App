package com.cwb.freshmeter.ui.analyze

import android.os.Bundle
import android.transition.TransitionInflater
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cwb.freshmeter.api.InferenceData
import com.cwb.freshmeter.databinding.ActivityDetailAnalyzeBinding
import com.squareup.picasso.Picasso

@Suppress("DEPRECATION")
class DetailAnalyzeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailAnalyzeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.sharedElementEnterTransition = TransitionInflater.from(this).inflateTransition(android.R.transition.move)
        binding = ActivityDetailAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inferenceData = intent.getParcelableExtra<InferenceData>("inference_data")
        if (inferenceData != null) {
            // Load the cropped image using Picasso
            if (inferenceData.cropped_img.isNotEmpty()) {
                Picasso.get().load(inferenceData.cropped_img).into(binding.ivDetailInference)
            }

            // Set the values for inference details
            binding.ivDetailInference.transitionName = "inference_image_transition"
            binding.tvFruitClass.text = inferenceData.fruit_class_string
            binding.tvConfidence.text = String.format("Confidence: %.2f%%", inferenceData.confidence * 100)
            binding.tvFreshnessPercentage.text = String.format("Freshness: %s", inferenceData.freshness_percentage)
            binding.tvFreshnessDays.text = String.format("Spoilage estimate: %d days", inferenceData.freshness_days)

            // Format tips as a numbered list
            val tips = inferenceData.tips.mapIndexed { index, tip -> "${index + 1}. $tip" }
            binding.tvTips.text = tips.joinToString(separator = "\n")
        } else {
            Toast.makeText(this, "No inference data found", Toast.LENGTH_SHORT).show()
        }
    }
}


