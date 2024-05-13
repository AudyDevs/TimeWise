package com.example.timewise.ui.tasks.view

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timewise.R
import com.example.timewise.databinding.ActivityTasksBinding
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.model.TaskModel
import com.example.timewise.ui.detail.DetailTaskActivity
import com.example.timewise.ui.dialog.DialogAddTask
import com.example.timewise.ui.dialog.DialogLabel
import com.example.timewise.ui.tasks.adapter.TasksAdapter
import com.example.timewise.ui.tasks.adapter.TasksAdapterFinished
import com.example.timewise.ui.tasks.viewmodel.TasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TasksActivity : AppCompatActivity() {

    private val tasksViewModel by viewModels<TasksViewModel>()
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var tasksAdapterFinished: TasksAdapterFinished
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
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    private fun initViewModel() {
        tasksViewModel.getLabelID(idLabel)
    }

    private fun initIntents() {
        idLabel = intent?.extras?.getInt("id") ?: 0
    }

    private fun initAdapter() {
        tasksAdapter = TasksAdapter(
            onItemSelected = { tasksModel ->
                navigateToDetailTaskActivity(tasksModel.id)
            },
            onUpdateFinished = { id, isFinished ->
                tasksViewModel.updateTaskFinished(id, idLabel, isFinished)
            }, onUpdateFavourite = { id, isFavourite ->
                tasksViewModel.updateTaskFavourite(id, idLabel, isFavourite)
            })

        tasksAdapterFinished = TasksAdapterFinished(
            onItemSelected = { tasksModel ->
                navigateToDetailTaskActivity(tasksModel.id)
            },
            onUpdateFinished = { id, isFinished ->
                tasksViewModel.updateTaskFinished(id, idLabel, isFinished)
            }, onUpdateFavourite = { id, isFavourite ->
                tasksViewModel.updateTaskFavourite(id, idLabel, isFavourite)
            })

        binding.rvTasks.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tasksAdapter
        }

        binding.rvTasksFinished.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = tasksAdapterFinished
        }
    }

    private fun initListeners() {
        binding.apply {
            btnEdit.setOnClickListener { showDialogEdit() }
            btnDelete.setOnClickListener { showDialogDelete() }
            btnBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
            fbAdd.setOnClickListener {
                DialogAddTask(
                    context = this@TasksActivity,
                    labelModel = labelModel,
                    onClickButtonAdd = { taskModel ->
                        tasksViewModel.insertTask(taskModel)
                    }
                )
            }
            layoutCompleted.setOnClickListener {
                showListFinished()
            }
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                tasksViewModel.label.collect {
                    if (it != null) {
                        labelModel = it
                        binding.etName.text = labelModel.name
                        changeUIColor()
                        tasksViewModel.getTasks(labelModel.id)
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                tasksViewModel.tasks.collect { taskModel ->
                    val responseNoFinished = taskModel.filter { !it.isFinished }
                    val responseFinished = taskModel.filter { it.isFinished }
                    tasksAdapter.updateList(responseNoFinished)
                    tasksAdapterFinished.updateList(responseFinished)
                    showCompleted(responseFinished)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun showCompleted(responseFinished: List<TaskModel>) {
        if (responseFinished.isNotEmpty()) {
            binding.tvCompleted.text =
                getString(R.string.completed) + " " + responseFinished.size.toString()
            binding.layoutCompleted.isVisible = true
        } else {
            binding.layoutCompleted.isVisible = false
        }
    }

    private fun changeUIColor() {
        binding.apply {
            main.setBackgroundColor(labelModel.backcolor)
            toolbar.setBackgroundColor(labelModel.backcolor)
            etName.setTextColor(labelModel.textColor)
            btnEdit.imageTintList = ColorStateList.valueOf(labelModel.textColor)
            btnEdit.setBackgroundColor(labelModel.backcolor)
            btnDelete.imageTintList = ColorStateList.valueOf(labelModel.textColor)
            btnDelete.setBackgroundColor(labelModel.backcolor)
            btnBack.imageTintList = ColorStateList.valueOf(labelModel.textColor)
            btnBack.setBackgroundColor(labelModel.backcolor)
            fbAdd.imageTintList = ColorStateList.valueOf(labelModel.backcolor)
            fbAdd.backgroundTintList = ColorStateList.valueOf(labelModel.textColor)
            imageCompleted.imageTintList = ColorStateList.valueOf(labelModel.textColor)
            tvCompleted.setTextColor(labelModel.textColor)
        }
    }

    private fun showDialogEdit() {
        DialogLabel(
            context = this,
            labelModel = labelModel,
            onClickButtonAdd = {
                tasksViewModel.updateLabel(it)
            })
    }

    private fun showDialogDelete() {
        val builder = AlertDialog.Builder(this)
            .setTitle(R.string.dialogDeleteTitle)
            .setMessage(R.string.dialogDeleteMessage)
            .setCancelable(false)
            .setPositiveButton(
                R.string.dialogDeleteNo
            ) { _, _ -> deleteLabel() }
            .setNegativeButton(R.string.dialogDeleteYes) { _, _ -> }
            .create()
        builder.show()
        builder.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(this, R.color.error))
    }

    private fun showListFinished() {
        binding.apply {
            if (rvTasksFinished.isVisible) {
                rvTasksFinished.isVisible = false
                imageCompleted.setImageResource(R.drawable.ic_arrow_right)
            } else {
                rvTasksFinished.isVisible = true
                imageCompleted.setImageResource(R.drawable.ic_arrow_down)
            }
        }
    }

    private fun deleteLabel() {
        tasksViewModel.deleteLabel(labelModel)
        onBackPressedDispatcher.onBackPressed()
    }

    private fun navigateToDetailTaskActivity(id: Int) {
        val intent = Intent(this, DetailTaskActivity::class.java)
        intent.putExtra("id", id)
        startActivity(intent)
    }
}