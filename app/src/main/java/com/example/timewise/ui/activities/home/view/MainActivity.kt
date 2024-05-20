package com.example.timewise.ui.activities.home.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timewise.R
import com.example.timewise.core.FilterTypes
import com.example.timewise.databinding.ActivityMainBinding
import com.example.timewise.ui.activities.filtered.view.FilteredTasksActivity
import com.example.timewise.ui.activities.home.adapter.HomeAdapter
import com.example.timewise.ui.activities.home.viewmodel.HomeViewModel
import com.example.timewise.ui.activities.search.view.SearchActivity
import com.example.timewise.ui.activities.tasks.view.TasksActivity
import com.example.timewise.ui.dialog.DialogLabel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val homeViewModel by viewModels<HomeViewModel>()
    private lateinit var homeAdapter: HomeAdapter
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
        initAdapter()
        initListeners()
        initUIState()
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    private fun initViewModel() {
        homeViewModel.getLabels()
        homeViewModel.getNumberTasks()
    }

    private fun initTheme() {
        AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)
        delegate.applyDayNight()
    }

    private fun initAdapter() {
        homeAdapter = HomeAdapter(onItemSelected = { labelModel ->
            navigateToTasksActivity(labelModel.id, labelModel.textColor)
        })

        binding.rvLabels.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = homeAdapter
        }
    }

    private fun initListeners() {
        binding.frameAddLabel.setOnClickListener {
            DialogLabel(
                context = this,
                labelModel = null,
                onClickButtonAdd = {
                    homeViewModel.insertLabel(it)
                })
        }
        binding.ButtonSearch.setOnClickListener {
            navigateToSearchActivity()
        }
        binding.layoutToday.setOnClickListener {
            navigateToFilteredTasksActivity(FilterTypes.Today.name)
        }
        binding.layoutWeek.setOnClickListener {
            navigateToFilteredTasksActivity(FilterTypes.Week.name)
        }
        binding.layoutLater.setOnClickListener {
            navigateToFilteredTasksActivity(FilterTypes.Later.name)
        }
        binding.layoutExpired.setOnClickListener {
            navigateToFilteredTasksActivity(FilterTypes.Expired.name)
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.labels.collect { labelModel ->
                    homeAdapter.updateList(labelModel)
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.tasksToday.collect { numberTasksToday ->
                    if (numberTasksToday > 0) {
                        binding.numberToday.text = numberTasksToday.toString()
                    } else {
                        binding.numberToday.text = ""
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.tasksWeek.collect { numberTasksWeek ->
                    if (numberTasksWeek > 0) {
                        binding.numberWeek.text = numberTasksWeek.toString()
                    } else {
                        binding.numberWeek.text = ""
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.tasksLater.collect { numberTasksLater ->
                    if (numberTasksLater > 0) {
                        binding.numberLater.text = numberTasksLater.toString()
                    } else {
                        binding.numberLater.text = ""
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                homeViewModel.tasksExpired.collect { numberTasksExpired ->
                    if (numberTasksExpired > 0) {
                        binding.numberExpired.text = numberTasksExpired.toString()
                    } else {
                        binding.numberExpired.text = ""
                    }
                }
            }
        }
    }

    private fun navigateToSearchActivity() {
        val intent = Intent(this, SearchActivity::class.java)
        startActivity(intent)
    }

    private fun navigateToFilteredTasksActivity(filterType: String) {
        val intent = Intent(this, FilteredTasksActivity::class.java)
        intent.putExtra("filterType", filterType)
        startActivity(intent)
    }

    private fun navigateToTasksActivity(id: Int, textColor: Int) {
        val intent = Intent(this, TasksActivity::class.java)
        intent.putExtra("id", id)
        intent.putExtra("textColor", textColor)
        startActivity(intent)
    }
}