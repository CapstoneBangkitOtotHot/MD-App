package com.cwb.freshmeter.ui.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.cwb.freshmeter.R
import com.cwb.freshmeter.databinding.ActivityProfileBinding
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.material.switchmaterial.SwitchMaterial

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding

    private val pref by lazy { PrefHelper(this) }

    private fun saveImageUri(uri: Uri?) {
        val sharedPref = getSharedPreferences("pref_profile", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString("profile_image_uri", uri?.toString())
            apply()
        }
    }
    private fun loadImageUri(): Uri? {
        val sharedPref = getSharedPreferences("pref_profile", Context.MODE_PRIVATE)
        val uriString = sharedPref.getString("profile_image_uri", null)
        return if (uriString != null) Uri.parse(uriString) else null
    }
    private fun saveEditTextValue(value: String) {
        val sharedPref = getSharedPreferences("pref_username", Context.MODE_PRIVATE)
        with (sharedPref.edit()) {
            putString("edit_text_value", value)
            apply()
        }
    }

    private fun loadEditTextValue(): String? {
        val sharedPref = getSharedPreferences("pref_username", Context.MODE_PRIVATE)
        return sharedPref.getString("edit_text_value", "") // Default empty string
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val switchMaterial = findViewById<SwitchMaterial>(R.id.switch_material)

        switchMaterial.isChecked = pref.getBoolean("pref_is_dark_mode")

        switchMaterial.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                pref.put("pref_is_dark_mode", true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            else {
                pref.put("pref_is_dark_mode", false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        val savedUri = loadImageUri()
        if (savedUri != null) {
            binding.setProfilePhoto.setImageURI(savedUri)
        }

        binding.EditPhotoButton.setOnClickListener{
            ImagePicker.with(this)
                .crop()	    			//Crop image(Optional), Check Customization for more option
                .compress(1024)			//Final image size will be less than 1 MB(Optional)
                .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                .start()
        }

        val savedUsername = loadEditTextValue()
        binding.EditUsername.setText(savedUsername)

        setupAction()
    }

    private fun setupAction() {
        binding.settingImageView.setOnClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        binding.setProfilePhoto.setImageURI(data?.data)
        if (resultCode == Activity.RESULT_OK) {
            binding.setProfilePhoto.setImageURI(data?.data)
            saveImageUri(data?.data)
        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onStop() {
        super.onStop()
        val currentText = binding.EditUsername.text.toString()
        saveEditTextValue(currentText)
    }
}