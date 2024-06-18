package com.cwb.freshmeter.ui.camera

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cwb.freshmeter.R
import com.cwb.freshmeter.api.InferenceResponse
import com.cwb.freshmeter.api.RetrofitClient
import com.cwb.freshmeter.databinding.ActivityCameraBinding
import com.cwb.freshmeter.ui.analyze.AnalyzeActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

@Suppress("DEPRECATION")
class CameraActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCameraBinding
    private var imageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnChooseImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        binding.btnAnalyze.setOnClickListener {
            if (imageUri != null) {
                binding.progressBar.visibility = View.VISIBLE
                analyzeImage(imageUri!!)
            } else {
                Toast.makeText(this, "Please choose an image first", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("This method has been deprecated...")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                imageUri = data?.data
                binding.ivPhoto.setImageURI(imageUri)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    @Deprecated("This method has been deprecated...")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun analyzeImage(imageUri: Uri) {
        val photoFile = File(imageUri.path!!)
        if (!photoFile.exists()) {
            Log.e("CameraActivity", "Photo file does not exist: ${photoFile.absolutePath}")
            Toast.makeText(this, "File does not exist", Toast.LENGTH_SHORT).show()
            binding.progressBar.visibility = View.GONE
            return
        }

        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), photoFile)
        val imageBody = MultipartBody.Part.createFormData("image", photoFile.name, requestFile)

        Log.d("CameraActivity", "Sending image: ${photoFile.absolutePath}")

        RetrofitClient.apiService.infer(imageBody)
            .enqueue(object : Callback<InferenceResponse> {
                override fun onResponse(call: Call<InferenceResponse>, response: Response<InferenceResponse>) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        Toast.makeText(this@CameraActivity, "Analysis successful", Toast.LENGTH_SHORT).show()
                        val inferenceResponse = response.body()
                        if (inferenceResponse != null) {
                            val intent = Intent(this@CameraActivity, AnalyzeActivity::class.java)
                            intent.putExtra("data", inferenceResponse)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                        } else {
                            Toast.makeText(this@CameraActivity, "Empty response", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.e("CameraActivity", "Analysis failed with status: ${response.code()}")
                        Toast.makeText(this@CameraActivity, "Analysis failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<InferenceResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Log.e("CameraActivity", "API call failed: ${t.message}", t)
                    Toast.makeText(this@CameraActivity, "API call failed", Toast.LENGTH_SHORT).show()
                }
            })
    }
}



