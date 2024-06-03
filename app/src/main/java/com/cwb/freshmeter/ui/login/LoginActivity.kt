package com.cwb.freshmeter.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.cwb.freshmeter.RegisterActivity
import com.cwb.freshmeter.databinding.ActivityLoginBinding
import com.cwb.freshmeter.ui.profile.PrefHelper
import com.cwb.freshmeter.ui.profile.ProfileActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val prefDarkMode by lazy { PrefHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SubmitLoginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()

            if (email.isEmpty()) {
                binding.emailEditText.error = "Email required"
                binding.emailEditText.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.passwordEditText.error = "Password required"
                binding.passwordEditText.requestFocus()
                return@setOnClickListener
            }

            CoroutineScope(Dispatchers.IO).launch {
                val loginSuccessful = AuthRepository.login(this@LoginActivity, email, password)
                if (!loginSuccessful) {
                    runOnUiThread {
                        Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        binding.MoveToRegister.setOnClickListener { onClickMoveToRegister() }

        // Optionally, set dark mode based on saved preference if you want to apply it on the login screen as well
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        val savedEmail = sharedPreferences.getString("user_email", null)
        if (savedEmail != null) {
            when (prefDarkMode.getBoolean("pref_is_dark_mode", savedEmail)) {
                true -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                }
                false -> {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            }
        }
    }

    private fun onClickMoveToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}