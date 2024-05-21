package com.example.timewise.ui.activities.filtered.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timewise.R
import com.example.timewise.core.FilterTypes
import com.example.timewise.databinding.ActivityFilteredTasksBinding
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.ui.activities.detail.view.DetailTaskActivity
import com.example.timewise.ui.activities.filtered.adapter.FilteredTasksAdapter
import com.example.timewise.ui.activities.filtered.viewmodel.FilteredTasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class FilteredTasksActivity : AppCompatActivity() {

    private val filteredTasksViewModel by viewModels<FilteredTasksViewModel>()
    private lateinit var filteredTasksAdapter: FilteredTasksAdapter
    private lateinit var binding: ActivityFilteredTasksBinding
    private var filterType = ""
    private lateinit var labelModel: List<LabelModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityFilteredTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }

    private fun initUI() {
        initIntent()
        initAdapter()
        initListeners()
        initUIState()
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    private fun initViewModel() {
        filteredTasksViewModel.getLabels()
        filteredTasksViewModel.filterTypes = FilterTypes.valueOf(filterType)
        filteredTasksViewModel.getFilteredTasks()
    }

    private fun initIntent() {
        filterType = intent?.extras?.getString("filterType") ?: FilterTypes.Today.name
    }

    private fun initAdapter() {
        filteredTasksAdapter = FilteredTasksAdapter(onItemSelected = { tasksModel ->
            navigateToDetailTaskActivity(tasksModel.id)
        }, onUpdateFinished = { id, isFinished ->
            filteredTasksViewModel.updateTaskFinished(id, isFinished)
        }, onUpdateFavourite = { id, isFavourite ->
            filteredTasksViewModel.updateTaskFavourite(id, isFavourite)
        })

        binding.rvFilteredTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = filteredTasksAdapter
        }
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                filteredTasksViewModel.tasks.collect { taskModel ->
                    binding.layoutStateFilter.isVisible = taskModel.isEmpty()
                    filteredTasksAdapter.updateList(taskModel)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                filteredTasksViewModel.labels.collect {
                    labelModel = it
                    filteredTasksAdapter.updateListLabel(labelModel)
                }
            }
        }
    }

    private fun navigateToDetailTaskActivity(idTask: Int) {
        val intent = Intent(this, DetailTaskActivity::class.java)
        intent.putExtra("idTask", idTask)
        startActivity(intent)
    }
}

