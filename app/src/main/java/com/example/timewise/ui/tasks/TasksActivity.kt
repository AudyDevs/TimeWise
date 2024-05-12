package com.example.timewise.ui.tasks

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.timewise.R
import com.example.timewise.databinding.ActivityTasksBinding
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.ui.detail.DetailTaskActivity
import com.example.timewise.ui.tasks.viewmodel.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksActivity : AppCompatActivity() {

    private val tasksViewModel by viewModels<TasksViewModel>()
    private lateinit var binding: ActivityTasksBinding
    private var idLabel: Int = 0
    private lateinit var labelModel: LabelModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityTasksBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        initUI()
    }

    private fun initUI() {
        initIntents()
        initAdapter()
        initListeners()
        initUIState()
        initViewModel()
    }

    private fun initIntents() {
        idLabel = intent?.extras?.getInt("id") ?: 0
    }

    private fun initAdapter() {

    }

    private fun initListeners() {
        binding.toolbar.setOnMenuItemClickListener { menu ->
            when (menu.itemId) {
                R.id.editLabel -> {}
                R.id.deleteLabel -> showDeleteDialog()
            }
            true
        }
        binding.toolbar.setNavigationOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    private fun showDeleteDialog() {
        AlertDialog.Builder(this)
            .setTitle(R.string.dialogDeleteTitle)
            .setMessage(R.string.dialogDeleteMessage)
            .setCancelable(false)
            .setPositiveButton(
                R.string.dialogDeleteNo
            ) { _, _ -> deleteLabel() }
            .setNegativeButton(R.string.dialogDeleteYes) { _, _ -> }
            .show()
    }

    private fun deleteLabel() {
        tasksViewModel.deleteLabel(labelModel)
        onBackPressedDispatcher.onBackPressed()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                tasksViewModel.label.collect {
                    if (it != null) {
                        labelModel = it
                        binding.toolbar.title = labelModel.name
                        // cuando tengamos valor cargamos la lista del recyclerVIEW, a demás tambein cargaremos los colores de la UI.
                    }
                }
            }
        }
    }

    private fun initViewModel() {
        tasksViewModel.getLabelID(idLabel)
    }

    private fun navigateToDetailTaskActivity() {
        val intent = Intent(this, DetailTaskActivity::class.java)
        startActivity(intent)
    }
}