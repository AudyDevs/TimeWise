package com.example.timewise.ui.activities.search.view

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timewise.R
import com.example.timewise.core.extensions.showKeyboard
import com.example.timewise.databinding.ActivitySearchBinding
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.ui.activities.detail.view.DetailTaskActivity
import com.example.timewise.ui.activities.search.adapter.SearchAdapter
import com.example.timewise.ui.activities.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val searchedViewModel by viewModels<SearchViewModel>()
    private lateinit var searchedAdapter: SearchAdapter
    private lateinit var binding: ActivitySearchBinding
    private lateinit var labelModel: List<LabelModel>

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
        initAdapter()
        initListeners()
        initUIState()
        initSearcher()
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    private fun initViewModel() {
        searchedViewModel.getLabels()
        searchedViewModel.getSearchedTasks()
    }

    private fun initAdapter() {
        searchedAdapter = SearchAdapter(
            onItemSelected = { tasksModel ->
                navigateToDetailTaskActivity(tasksModel.id)
            },
            onUpdateFinished = { id, isFinished ->
                searchedViewModel.updateTaskFinished(id, isFinished)
            }, onUpdateFavourite = { id, isFavourite ->
                searchedViewModel.updateTaskFavourite(id, isFavourite)
            })

        binding.rvSearchedTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = searchedAdapter
        }
    }

    private fun initListeners() {
        binding.btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.btnReset.setOnClickListener {
            binding.etSearch.setText("")
            searchedViewModel.search = ""
            searchedViewModel.getSearchedTasks()
        }
        binding.etSearch.addTextChangedListener { text ->
            searchedViewModel.search = text.toString()
            searchedViewModel.getSearchedTasks()
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchedViewModel.tasks.collect { taskModel ->
                    binding.layoutStateSearch.isVisible = taskModel.isEmpty()
                    searchedAdapter.updateList(taskModel)
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchedViewModel.labels.collect {
                    labelModel = it
                    searchedAdapter.updateListLabel(labelModel)
                }
            }
        }
    }

    private fun initSearcher() {
        binding.apply {
            etSearch.requestFocus()
            etSearch.postDelayed({
                etSearch.context.showKeyboard(etSearch)
            }, 0)
        }
    }

    private fun navigateToDetailTaskActivity(idTask: Int) {
        val intent = Intent(this, DetailTaskActivity::class.java)
        intent.putExtra("idTask", idTask)
        startActivity(intent)
    }
}