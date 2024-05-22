package com.cwb.freshmeter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var moveToCameraButton: Button
    private lateinit var moveToProfileButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moveToCameraButton = findViewById(R.id.camera)
        moveToProfileButton = findViewById(R.id.ProfileButton)

        moveToCameraButton.setOnClickListener { onClickMoveToCamera() }
        moveToProfileButton.setOnClickListener { onClickMoveToProfile() }
    }

    private fun onClickMoveToCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    private fun onClickMoveToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }
}