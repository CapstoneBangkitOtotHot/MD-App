package com.cwb.freshmeter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class LoginActivity : AppCompatActivity() {
    private lateinit var moveTologinButton: Button
    private lateinit var moveToregisterButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        moveTologinButton = findViewById(R.id.SubmitLoginButton)
        moveToregisterButton = findViewById(R.id.MoveToRegister)

        moveTologinButton.setOnClickListener { onLoginButtonClicked() }
        moveToregisterButton.setOnClickListener { onClickMoveToRegister() }
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