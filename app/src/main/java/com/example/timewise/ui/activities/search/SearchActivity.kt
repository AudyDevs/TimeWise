package com.example.timewise.ui.activities.search

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.timewise.R
import com.example.timewise.databinding.ActivitySearchBinding
import com.example.timewise.ui.activities.detail.view.DetailTaskActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }

    private fun initUI() {
        initListeners()
    }

    private fun initListeners() {
//        binding.ButtonDetail.setOnClickListener {
//            navigateToDetailTaskActivity()
//        }
//        binding.ButtonReturn.setOnClickListener {
//            onBackPressedDispatcher.onBackPressed()
//        }
    }

    private fun navigateToDetailTaskActivity(){
        val intent = Intent(this, DetailTaskActivity::class.java)
        startActivity(intent)
    }
}