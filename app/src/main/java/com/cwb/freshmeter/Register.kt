package com.cwb.freshmeter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Register : AppCompatActivity() {

    private lateinit var moveToLoginButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        moveToLoginButton = findViewById(R.id.MoveToLogin)

        moveToLoginButton.setOnClickListener { onClickMoveToLogin() }
    }

    private fun onClickMoveToLogin() {
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
    }
}