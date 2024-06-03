package com.cwb.freshmeter

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cwb.freshmeter.api.RegisterRequest
import com.cwb.freshmeter.api.RegisterResponse
import com.cwb.freshmeter.api.RetrofitClient
import com.cwb.freshmeter.databinding.ActivityRegisterBinding
import com.cwb.freshmeter.ui.login.LoginActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.SubmitRegisterButton.setOnClickListener {
            registerUser()
        }

        binding.MoveToLogin.setOnClickListener { onClickMoveToLogin() }
    }

    private fun onClickMoveToLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    private fun registerUser() {
        val email = binding.emailEditText.text.toString().trim()
        val password = binding.passwordEditText.text.toString().trim()
        val confirmPassword = binding.repasswordEditText.text.toString().trim()

        if (email.isEmpty()) {
            binding.emailEditText.error = "Email is required"
            binding.emailEditText.requestFocus()
            return
        }

        if (password.isEmpty()) {
            binding.passwordEditText.error = "Password is required"
            binding.passwordEditText.requestFocus()
            return
        }

        if (confirmPassword.isEmpty()) {
            binding.repasswordEditText.error = "Please confirm your password"
            binding.repasswordEditText.requestFocus()
            return
        }

        if (password != confirmPassword) {
            binding.repasswordEditText.error = "Passwords do not match"
            binding.repasswordEditText.requestFocus()
            return
        }

        val request = RegisterRequest(email, password)
        RetrofitClient.instance.registerUser(request)
            .enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>) {
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        if (registerResponse?.status == "error" && registerResponse.message == "Failed to register, email is already exists") {
                            Toast.makeText(this@RegisterActivity, "Email already registered", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this@RegisterActivity, registerResponse?.message ?: "Registration successful", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Toast.makeText(this@RegisterActivity, "Registration failed", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    Toast.makeText(this@RegisterActivity, t.message ?: "Registration failed", Toast.LENGTH_SHORT).show()
                }
            })
    }
}