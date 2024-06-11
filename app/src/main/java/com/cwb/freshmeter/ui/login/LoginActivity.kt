package com.cwb.freshmeter.ui.login

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.cwb.freshmeter.R
import com.cwb.freshmeter.ui.register.RegisterActivity
import com.cwb.freshmeter.databinding.ActivityLoginBinding
import com.cwb.freshmeter.ui.forgotPassword.ForgotPasswordActivity
import com.cwb.freshmeter.ui.profile.PrefHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val prefDarkMode by lazy { PrefHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animateView()

        binding.btnSubmitLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (email.isEmpty()) {
                binding.etEmail.error = "Email required"
                binding.etEmail.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                binding.etPassword.error = "Password required"
                binding.etPassword.requestFocus()
                return@setOnClickListener
            }

            binding.progressBar.visibility = View.VISIBLE

            CoroutineScope(Dispatchers.IO).launch {
                val loginSuccessful = AuthRepository.login(this@LoginActivity, email, password)
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                if (!loginSuccessful) {
                    runOnUiThread {
                        binding.progressBar.visibility = View.GONE
                        if (!loginSuccessful) {
                            Toast.makeText(this@LoginActivity, "Login failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }

        binding.etPassword.setMinLength(8)

        binding.btnMoveToRegister.setOnClickListener {
            onClickMoveToRegister()
        }
        binding.btnMoveToForgotPassword.setOnClickListener {
            onCLickMoveToForgotPassword()
        }


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
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun onCLickMoveToForgotPassword() {
        val intent = Intent(this, ForgotPasswordActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun animateView() {
        binding.ivLogin.alpha = 0f
        binding.tvLogin.alpha = 0f
        binding.tvEmail.alpha = 0f
        binding.etEmail.alpha = 0f
        binding.tvPassword.alpha = 0f
        binding.etPassword.alpha = 0f
        binding.btnSubmitLogin.alpha = 0f
        binding.btnMoveToRegister.alpha = 0f
        binding.btnMoveToForgotPassword.alpha = 0f

        binding.ivLogin.animate().alpha(1f).setDuration(1000).start()
        binding.tvLogin.animate().alpha(1f).setDuration(1000).setStartDelay(200).start()
        binding.tvEmail.animate().alpha(1f).setDuration(1000).setStartDelay(400).start()
        binding.etEmail.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
        binding.tvPassword.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
        binding.etPassword.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
        binding.btnSubmitLogin.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
        binding.btnMoveToRegister.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
        binding.btnMoveToForgotPassword.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
    }
}