package com.cwb.freshmeter.ui.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cwb.freshmeter.CameraActivity
import com.cwb.freshmeter.R
import com.cwb.freshmeter.ui.profile.ProfileActivity

class MainActivity : AppCompatActivity() {

    private lateinit var moveToCameraButton: Button
    private lateinit var moveToProfileButton: Button

    private lateinit var recyclerView: RecyclerView
    private lateinit var articlesList: ArrayList<Articles>
    private lateinit var articlesAdapter: ArticlesAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        moveToCameraButton = findViewById(R.id.camera)
        moveToProfileButton = findViewById(R.id.ProfileButton)

        moveToCameraButton.setOnClickListener { onClickMoveToCamera() }
        moveToProfileButton.setOnClickListener { onClickMoveToProfile() }

        init()
    }

    private fun onClickMoveToCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
    }

    private fun onClickMoveToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        startActivity(intent)
    }

    private fun init() {
        recyclerView = findViewById(R.id.rv_articles)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        articlesList = ArrayList()

        addDataToList()

        articlesAdapter = ArticlesAdapter(articlesList, this)
        recyclerView.adapter = articlesAdapter
    }

    private fun addDataToList() {
        articlesList.add(Articles(R.drawable.article_1, "Manfaat Buah dan Sayuran", "https://krakataumedika.com/info-media/artikel/manfaat-buah-dan-sayuran-untuk-kesehatan"))
        articlesList.add(Articles(R.drawable.article_2, "Mengelola Buah dan Sayur", "https://www.rskariadi.co.id/news/102/MENGOLAH-BUAH-DAN-SAYUR-DENGAN-BENAR/Artikel"))
        articlesList.add(Articles(R.drawable.article_3, "Rekomendasi Masakan", "https://www.fimela.com/food/read/5039289/10-resep-masakan-sayur-enak-dan-praktis-cocok-untuk-menu-sehari-hari?page=4"))
    }
}