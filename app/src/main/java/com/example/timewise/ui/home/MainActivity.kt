package com.example.timewise.ui.home

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.timewise.R
import com.example.timewise.databinding.ActivityMainBinding
import com.example.timewise.ui.search.SearchActivity
import com.example.timewise.ui.tasks.TasksActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }

    private fun initUI() {
        initTheme()
        initListeners()
    }

    private fun initTheme() {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        delegate.applyDayNight()
    }

    private fun initListeners() {
        binding.ButtonSearch.setOnClickListener {
            navigateToSearchActivity()
        }
        binding.layoutToday.setOnClickListener {
            navigateToTasksActivity()
        }
        binding.layoutWeek.setOnClickListener {
            navigateToTasksActivity()
        }
        binding.layoutLater.setOnClickListener {
            navigateToTasksActivity()
        }
        binding.layoutExpired.setOnClickListener {
            navigateToTasksActivity()
        }
    }

    private fun navigateToSearchActivity(){
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }
    private fun navigateToTasksActivity(){
        val intent = Intent(this, TasksActivity::class.java)
        startActivity(intent)
    }
}