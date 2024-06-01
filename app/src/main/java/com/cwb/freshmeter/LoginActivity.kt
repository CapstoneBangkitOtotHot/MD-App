package com.cwb.freshmeter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatDelegate
import com.cwb.freshmeter.ui.main.MainActivity
import com.cwb.freshmeter.ui.profile.PrefHelper

class LoginActivity : AppCompatActivity() {
    private lateinit var moveTologinButton: Button
    private lateinit var moveToregisterButton: Button

    private val pref by lazy { PrefHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        moveTologinButton = findViewById(R.id.SubmitLoginButton)
        moveToregisterButton = findViewById(R.id.MoveToRegister)

        moveTologinButton.setOnClickListener { onLoginButtonClicked() }
        moveToregisterButton.setOnClickListener { onClickMoveToRegister() }

        when (pref.getBoolean("pref_is_dark_mode")) {
            true -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }
            false -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun onLoginButtonClicked() {
        // Perform login logic here (e.g., validate credentials)
        // Assuming successful login, navigate to MainActivity

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun onClickMoveToRegister() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}