package com.cwb.freshmeter

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {

    private lateinit var moveToCameraButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moveToCameraButton = findViewById(R.id.camera)

        moveToCameraButton.setOnClickListener { onClickMoveToCamera() }
    }

    private fun onClickMoveToCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }
}