package com.cwb.freshmeter.ui.profile

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.cwb.freshmeter.R
import com.cwb.freshmeter.api.RetrofitClient
import com.cwb.freshmeter.databinding.ActivityProfileBinding
import com.cwb.freshmeter.ui.login.AuthRepository
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.switchmaterial.SwitchMaterial
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var email: String
    private val pref by lazy { PrefHelper(this) }

    private fun saveImageUri(uri: Uri?) {
        uri?.toString()?.let { pref.putString("profile_image_uri", it, email) }
    }

    private fun loadImageUri(): Uri? {
        val uriString = pref.getString("profile_image_uri", email)
        return if (uriString != null) Uri.parse(uriString) else null
    }

    private fun saveEditTextValue(value: String) {
        pref.putString("edit_text_value", value, email)
    }

    private fun loadEditTextValue(): String? {
        return pref.getString("edit_text_value", email)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        email = intent.getStringExtra("user_email") ?: "default@example.com"

        val switchMaterial = findViewById<SwitchMaterial>(R.id.switch_material)
        switchMaterial.isChecked = pref.getBoolean("pref_is_dark_mode", email)

        switchMaterial.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                pref.putBoolean("pref_is_dark_mode", true, email)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                pref.putBoolean("pref_is_dark_mode", false, email)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val savedUri = loadImageUri()
        if (savedUri != null) {
            binding.sivProfilePicture.setImageURI(savedUri)
        }

        binding.btnEditPhoto.setOnClickListener {
            ImagePicker.with(this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start()
        }

        val savedUsername = loadEditTextValue()
        binding.etEditUsername.setText(savedUsername)

        setupAction()

        binding.btnLogout.setOnClickListener {
            showLogoutConfirmationDialog()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun setupAction() {
        binding.ivSetLanguage.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    @Deprecated("This method has been deprecated in favor of using the Activity Result API\n      which brings increased type safety via an {@link ActivityResultContract} and the prebuilt\n      contracts for common intents available in\n      {@link androidx.activity.result.contract.ActivityResultContracts}, provides hooks for\n      testing, and allow receiving results in separate, testable classes independent from your\n      activity. Use\n      {@link #registerForActivityResult(ActivityResultContract, ActivityResultCallback)}\n      with the appropriate {@link ActivityResultContract} and handling the result in the\n      {@link ActivityResultCallback#onActivityResult(Object) callback}.")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.sivProfilePicture.setImageURI(data?.data)
        when (resultCode) {
            Activity.RESULT_OK -> {
                binding.sivProfilePicture.setImageURI(data?.data)
                saveImageUri(data?.data)
            }
            ImagePicker.RESULT_ERROR -> {
                Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
            }
            else -> {
                Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        val currentText = binding.etEditUsername.text.toString()
        saveEditTextValue(currentText)
    }

    private fun showLogoutConfirmationDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Logout")
        builder.setMessage("Are you sure you want to logout?")
        builder.setPositiveButton("Yes") { dialog, _ ->
            binding.progressBar.visibility = View.VISIBLE
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    val response = RetrofitClient.apiService.logout()
                    if (response.status == "ok") {
                        withContext(Dispatchers.Main) {
                            binding.progressBar.visibility = View.GONE
                            AuthRepository.logout(this@ProfileActivity)
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(this@ProfileActivity, "Logout failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                } catch (e: Exception) {
                    withContext(Dispatchers.Main) {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(this@ProfileActivity, "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        builder.create().show()
    }
}