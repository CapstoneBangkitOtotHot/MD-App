package com.cwb.freshmeter.ui.register

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cwb.freshmeter.R
import com.cwb.freshmeter.api.RegisterRequest
import com.cwb.freshmeter.api.RegisterResponse
import com.cwb.freshmeter.api.RetrofitClient
import com.cwb.freshmeter.databinding.ActivityRegisterBinding
import com.cwb.freshmeter.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Suppress("DEPRECATION")
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animateView()

        binding.btnSubmitRegister.setOnClickListener {
            registerUser()
        }

        binding.etPasswordRegister.setMinLength(8)
    }

    @Deprecated("This method has been deprecated...")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun registerUser() {
        val email = binding.etEmailRegister.text.toString().trim()
        val password = binding.etPasswordRegister.text.toString().trim()
        val confirmPassword = binding.etConfirmPassword.text.toString().trim()

        if (email.isEmpty()) {
            binding.etEmailRegister.error = "Email is required"
            binding.etEmailRegister.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.etPasswordRegister.error = "Password is required"
            binding.etPasswordRegister.requestFocus()
            return
        }

        if (confirmPassword.isEmpty()) {
            binding.etConfirmPassword.error = "Please confirm your password"
            binding.etConfirmPassword.requestFocus()
            return
        }

        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Passwords do not match"
            binding.etConfirmPassword.requestFocus()
            return
        }

        binding.progressBar.visibility = View.VISIBLE

        val request = RegisterRequest(email, password)
        RetrofitClient.apiService.registerUser(request)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    binding.progressBar.visibility = View.GONE
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        if (registerResponse?.status == "error" && registerResponse.message == "Failed to register, email is already exists") {
                            Toast.makeText(this@RegisterActivity, "Email already registered", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@RegisterActivity, registerResponse?.message ?: "Registration successful", Toast.LENGTH_SHORT).show()
                            // Move to LoginActivity and finish RegisterActivity
                            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
                            finish()
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    Toast.makeText(this@RegisterActivity, t.message ?: "Registration failed", Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun animateView() {
        binding.ivRegister.alpha = 0f
        binding.tvRegister.alpha = 0f
        binding.tvEmailRegister.alpha = 0f
        binding.etEmailRegister.alpha = 0f
        binding.tvPasswordRegister.alpha = 0f
        binding.etPasswordRegister.alpha = 0f
        binding.tvConfirmPassword.alpha = 0f
        binding.etConfirmPassword.alpha = 0f
        binding.btnSubmitRegister.alpha = 0f

        binding.ivRegister.animate().alpha(1f).setDuration(1000).start()
        binding.tvRegister.animate().alpha(1f).setDuration(1000).setStartDelay(200).start()
        binding.tvEmailRegister.animate().alpha(1f).setDuration(1000).setStartDelay(400).start()
        binding.etEmailRegister.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
        binding.tvPasswordRegister.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
        binding.etPasswordRegister.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
        binding.tvConfirmPassword.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
        binding.etConfirmPassword.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
        binding.btnSubmitRegister.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
    }
}