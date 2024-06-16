package com.cwb.freshmeter.ui.analyze

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.cwb.freshmeter.R
import com.cwb.freshmeter.api.InferenceResponse
import com.cwb.freshmeter.databinding.ActivityAnalyzeBinding
import com.example.burjoholic7.ui.transactions.AnalyzeAdapter

@Suppress("DEPRECATION")
class AnalyzeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAnalyzeBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAnalyzeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val inferenceResponse = intent.getParcelableExtra<InferenceResponse>("data")
        if (inferenceResponse != null) {
            Log.wtf("WTF!", inferenceResponse.status)
        }

        if (inferenceResponse != null) {
                val data = inferenceResponse.data
                val inferences = data.inferences

                Log.wtf("WTF!", inferenceResponse.status)
                Log.wtf("WTF!", data.orig_img)

                if (! inferences.isEmpty()) {
                    Log.wtf("WTF!", inferences[0].fruit_class.toString())
                    Log.wtf("WTF!", inferences[0].cropped_img)
                    Log.wtf("WTF!", inferences[0].confidence.toString())
                    Log.wtf("WTF!", inferences[0].freshness.toString())

                }


                binding.rvInferences.setHasFixedSize(true)
                binding.rvInferences.layoutManager = LinearLayoutManager(binding.root.context)

                val adapter = AnalyzeAdapter(this@AnalyzeActivity, inferences)
                binding.rvInferences.adapter = adapter
            // Proceed with using the inference data as needed
        } else {
            Toast.makeText(this, "No inference response found", Toast.LENGTH_SHORT).show()
        }

        // Get the image URI from the intent and display it
//        val imageUri = intent.getStringExtra("imageUri")?.let { Uri.parse(it) }
//        if (imageUri != null) {
//            binding.ivResult.setImageURI(imageUri)
//        }
    }

    @Deprecated("This method has been deprecated...")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }
}
