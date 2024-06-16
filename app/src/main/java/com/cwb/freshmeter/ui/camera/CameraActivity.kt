package com.cwb.freshmeter.ui.camera

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.Telephony.Mms.Part.FILENAME
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.cwb.freshmeter.R
import com.cwb.freshmeter.api.InferenceResponse
import com.cwb.freshmeter.api.RegisterResponse
import com.cwb.freshmeter.api.RetrofitClient
import com.cwb.freshmeter.databinding.ActivityCameraBinding
import com.cwb.freshmeter.ui.analyze.AnalyzeActivity
import com.cwb.freshmeter.ui.login.LoginActivity
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.common.util.concurrent.ListenableFuture
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.Locale

@Suppress("DEPRECATION")
class CameraActivity : AppCompatActivity() {
    companion object {
        private const val FILENAME = "yyyy-MM-dd-HH-mm-ss-SSS"
        private const val PHOTO_EXTENSION = ".jpg"
    }

    private val REQUEST_CODE_PERMISSIONS = 10
    private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)


    private lateinit var binding: ActivityCameraBinding
    private var imageUri: Uri? = null

    private lateinit var cameraProviderFuture : ListenableFuture<ProcessCameraProvider>
    private var imageCapture: ImageCapture? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (allPermissionsGranted()) {
            startCamera()
        } else {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        binding.btnChooseImage.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        binding.btnAnalyze.setOnClickListener {
            imageCapture?.let { capture ->
                val photoFile = createFile(applicationContext.filesDir, FILENAME, PHOTO_EXTENSION)
                val outputOptions = ImageCapture.OutputFileOptions.Builder(photoFile).build()

                capture.takePicture(outputOptions, ContextCompat.getMainExecutor(this), object : ImageCapture.OnImageSavedCallback {
                    override fun onError(exc: ImageCaptureException) {
                        Log.wtf("WTF!", "Photo capture failed: ${exc.message}", exc)
                        Toast.makeText(applicationContext, "Photo capture failed", Toast.LENGTH_SHORT).show()
                    }



                    override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                        val requestFile = RequestBody.create(MediaType.parse("image/jpeg"), photoFile)
                        val imageBody = MultipartBody.Part.createFormData("image", photoFile.name, requestFile)

                        RetrofitClient.apiService.infer(imageBody)
                            .enqueue(object : Callback<InferenceResponse> {
                                override fun onResponse(call: Call<InferenceResponse>, response: Response<InferenceResponse>) {
                                    if (response.isSuccessful) {
                                        Toast.makeText(this@CameraActivity, "Wow", Toast.LENGTH_SHORT).show()

                                        val inferenceResponse = response.body()
                                        if (inferenceResponse != null) {
                                            val data = inferenceResponse.data
                                            val inferences = data.inferences
                                            Log.wtf("WTF!", "REQUEST")
                                            Log.wtf("WTF!", inferenceResponse.status)
                                            Log.wtf("WTF!", data.orig_img)

                                            if (! inferences.isEmpty()) {
                                                Log.wtf("WTF!", inferences[0].fruit_class.toString())
                                                Log.wtf("WTF!", inferences[0].cropped_img)
                                                Log.wtf("WTF!", inferences[0].confidence.toString())
                                                Log.wtf("WTF!", inferences[0].freshness.toString())

                                            }
                                            Log.wtf("WTF!", "END OF REQUEST ----")

                                            val intent = Intent(this@CameraActivity, AnalyzeActivity::class.java)
                                            intent.putExtra("data", inferenceResponse)
                                            startActivity(intent)
                                        } else {
                                            Toast.makeText(this@CameraActivity, "Empty response", Toast.LENGTH_SHORT).show()
                                        }
                                    } else {
//                                        Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onFailure(call: Call<InferenceResponse>, t: Throwable) {

                                }
                            })
                    }
                })
            } ?: run {
                Toast.makeText(this, "Camera not ready", Toast.LENGTH_SHORT).show()
            }
//            imageUri?.let {
//                val intent = Intent(this, AnalyzeActivity::class.java).apply {
//                    putExtra("imageUri", it.toString())
//                }
//                startActivity(intent)
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
//            } ?: run {
//                Toast.makeText(this, "Please choose an image first", Toast.LENGTH_SHORT).show()
//            }
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

    private fun bindPreview(cameraProvider : ProcessCameraProvider) {
        var preview : Preview = Preview.Builder().build()
        imageCapture = ImageCapture.Builder().build()

        var cameraSelector : CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        preview.surfaceProvider = binding.previewView.surfaceProvider

        cameraProvider.bindToLifecycle(this as LifecycleOwner, cameraSelector, preview, imageCapture)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }

    private fun startCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener(Runnable {
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(this))
    }

    private fun createFile(baseFolder: File, format: String, extension: String) = File(
        baseFolder,
        SimpleDateFormat(format, Locale.US).format(System.currentTimeMillis()) + extension
    )

}
