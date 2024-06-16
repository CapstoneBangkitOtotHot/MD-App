package com.cwb.freshmeter.ui.main

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.cwb.freshmeter.ui.camera.CameraActivity
import com.cwb.freshmeter.R
import com.cwb.freshmeter.databinding.ActivityMainBinding
import com.cwb.freshmeter.ui.login.MyApp.Companion.context
import com.cwb.freshmeter.ui.main.articleItem.Articles
import com.cwb.freshmeter.ui.main.articleItem.ArticlesAdapter
import com.cwb.freshmeter.ui.main.scanItem.ScanAdapter
import com.cwb.freshmeter.ui.main.scanItem.ScanItem
import com.cwb.freshmeter.ui.profile.PrefHelper
import com.cwb.freshmeter.ui.profile.ProfileActivity

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var articlesList: ArrayList<Articles>
    private lateinit var articlesAdapter: ArticlesAdapter

    private lateinit var scanList: ArrayList<ScanItem>
    private lateinit var scanAdapter: ScanAdapter

    private lateinit var email: String
    private val prefDarkMode by lazy { PrefHelper(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the current user's email from shared preferences
        val sharedPreferences = getSharedPreferences("auth", Context.MODE_PRIVATE)
        email = sharedPreferences.getString("user_email", null) ?: "default@example.com"

        // Set dark mode based on saved preference
        when (prefDarkMode.getBoolean("pref_is_dark_mode", email)) {
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        binding.fbCamera.setOnClickListener { onClickMoveToCamera() }
        binding.ivProfile.setOnClickListener { onClickMoveToProfile() }

        init()
    }

    private fun onClickMoveToCamera() {
        val intent = Intent(this, CameraActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun onClickMoveToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.putExtra("user_email", email)
        startActivity(intent)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }

    private fun init() {
        binding.rvArticles.setHasFixedSize(true)
        binding.rvArticles.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        articlesList = ArrayList()
        addDataToList()

        articlesAdapter = ArticlesAdapter(articlesList, this)
        binding.rvArticles.adapter = articlesAdapter

        binding.rvScan.setHasFixedSize(true)
        binding.rvScan.layoutManager = GridLayoutManager(this, 2)
        scanList = ArrayList()
        addScanDataToList()

        scanAdapter = ScanAdapter(scanList, this)
        binding.rvScan.adapter = scanAdapter
    }

    private fun addDataToList() {
        val resources = context.resources
        articlesList.add(Articles(R.drawable.article_1, resources.getString(R.string.article_1_title), resources.getString(R.string.article_1_url)))
        articlesList.add(Articles(R.drawable.article_2, resources.getString(R.string.article_2_title), resources.getString(R.string.article_2_url)))
        articlesList.add(Articles(R.drawable.article_3, resources.getString(R.string.article_3_title), resources.getString(R.string.article_3_url)))
    }

    private fun addScanDataToList() {
        val resources = context.resources
        scanList.add(ScanItem(R.drawable.item_apple, resources.getString(R.string.scan_1)))
        scanList.add(ScanItem(R.drawable.item_mango, resources.getString(R.string.scan_2)))
        scanList.add(ScanItem(R.drawable.item_sapodilla, resources.getString(R.string.scan_3)))
    }
}
