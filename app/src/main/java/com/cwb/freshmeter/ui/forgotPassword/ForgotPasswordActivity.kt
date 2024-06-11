package com.cwb.freshmeter.ui.forgotPassword

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.cwb.freshmeter.R
import com.cwb.freshmeter.api.ForgotPasswordRequest
import com.cwb.freshmeter.api.RetrofitClient
import com.cwb.freshmeter.databinding.ActivityForgotPasswordBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Suppress("DEPRECATION")
class ForgotPasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        animateView()

        binding.btnSendCode.setOnClickListener {
            onClickSendCode()
        }
    }

    @Deprecated("This method has been deprecated in favor of using the\n      {@link OnBackPressedDispatcher} via {@link #getOnBackPressedDispatcher()}.\n      The OnBackPressedDispatcher controls how back button events are dispatched\n      to one or more {@link OnBackPressedCallback} objects.")
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    private fun onClickSendCode() {
        val email = binding.etEmail.text.toString().trim()
        if (email.isEmpty()) {
            binding.etEmail.error = "Email required"
            binding.etEmail.requestFocus()
            return
        }

        binding.progressBar.visibility = View.VISIBLE
        CoroutineScope(Dispatchers.IO).launch {
            val response = RetrofitClient.apiService.forgotPassword(ForgotPasswordRequest(email))
            withContext(Dispatchers.Main) {
                binding.progressBar.visibility = View.GONE
                if (response.status == "ok") {
                    Toast.makeText(this@ForgotPasswordActivity, response.message, Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ForgotPasswordActivity, "Failed to send password reset email", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun animateView() {
        binding.ivForgotPassword.alpha = 0f
        binding.tvForgotPassword.alpha = 0f
        binding.tvEmail.alpha = 0f
        binding.etEmail.alpha = 0f
        binding.btnSendCode.alpha = 0f

        binding.ivForgotPassword.animate().alpha(1f).setDuration(1000).start()
        binding.tvForgotPassword.animate().alpha(1f).setDuration(1000).setStartDelay(200).start()
        binding.tvEmail.animate().alpha(1f).setDuration(1000).setStartDelay(400).start()
        binding.etEmail.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
        binding.etEmail.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
        binding.btnSendCode.animate().alpha(1f).setDuration(1000).setStartDelay(600).start()
    }
}