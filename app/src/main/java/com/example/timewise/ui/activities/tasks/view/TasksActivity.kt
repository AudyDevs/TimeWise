package com.example.timewise.ui.activities.tasks.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timewise.R
import com.example.timewise.core.AlarmNotification
import com.example.timewise.core.Time
import com.example.timewise.databinding.ActivityTasksBinding
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.model.TaskModel
import com.example.timewise.ui.activities.detail.view.DetailTaskActivity
import com.example.timewise.ui.activities.tasks.adapter.TasksAdapter
import com.example.timewise.ui.activities.tasks.adapter.TasksAdapterFinished
import com.example.timewise.ui.activities.tasks.viewmodel.TasksViewModel
import com.example.timewise.ui.dialog.DialogAddTask
import com.example.timewise.ui.dialog.DialogDelete
import com.example.timewise.ui.dialog.DialogLabel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class TasksActivity : AppCompatActivity() {

    private val tasksViewModel by viewModels<TasksViewModel>()
    private lateinit var tasksAdapter: TasksAdapter
    private lateinit var tasksAdapterFinished: TasksAdapterFinished
    private lateinit var binding: ActivityTasksBinding
    private lateinit var labelModel: LabelModel
    private lateinit var taskModel: List<TaskModel>
    private var idLabel: Int = 0

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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
        idLabel = intent?.extras?.getInt("idLabel") ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initAdapter() {
        tasksAdapter = TasksAdapter(
            onItemSelected = { tasksModel ->
                navigateToDetailTaskActivity(tasksModel.id)
            },
            onUpdateFinished = { id, isFinished ->
                notificationTaskFinished(id, isFinished)
                tasksViewModel.updateTaskFinished(id, isFinished)
            }, onUpdateFavourite = { id, isFavourite ->
                tasksViewModel.updateTaskFavourite(id, isFavourite)
            })

        tasksAdapterFinished = TasksAdapterFinished(
            onItemSelected = { tasksModel ->
                navigateToDetailTaskActivity(tasksModel.id)
            },
            onUpdateFinished = { id, isFinished ->
                notificationTaskFinished(id, isFinished)
                tasksViewModel.updateTaskFinished(id, isFinished)
            }, onUpdateFavourite = { id, isFavourite ->
                tasksViewModel.updateTaskFavourite(id, isFavourite)
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
                        changeUIValues()
                        changeUIColor()
                        tasksViewModel.getTasks()
                    }
                }
            }
        }

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                tasksViewModel.tasks.collect { listTaskModel ->
                    taskModel = listTaskModel
                    val responseNoFinished = taskModel.filter { !it.isFinished }
                    val responseFinished = taskModel.filter { it.isFinished }
                    tasksAdapter.updateList(responseNoFinished)
                    tasksAdapterFinished.updateList(responseFinished)
                    showCompleted(responseFinished)
                }
            }
        }
    }

    private fun showCompleted(responseFinished: List<TaskModel>) {
        if (responseFinished.isNotEmpty()) {
            binding.tvCompleted.text =
                getString(R.string.completed, responseFinished.size.toString())
            binding.layoutCompleted.isVisible = true
        } else {
            binding.layoutCompleted.isVisible = false
        }
    }

    private fun changeUIValues() {
        binding.apply {
            etName.text = labelModel.name
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
        tasksAdapter.updateColor(labelModel.textColor)
        tasksAdapterFinished.updateColor(labelModel.textColor)
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
        DialogDelete(
            binding.main,
            R.string.dialogDeleteListMessage,
            onSelectedButton = { isSelected ->
                if (isSelected) {
                    deleteLabel()
                }
            })
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

    private fun navigateToDetailTaskActivity(idTask: Int) {
        val intent = Intent(this, DetailTaskActivity::class.java)
        intent.putExtra("idTask", idTask)
        startActivity(intent)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun notificationTaskFinished(id: Int, isFinished: Boolean) {
        val task = taskModel.find { it.id == id }
        if (task != null) {
            cancelNotification(task.id)
            if (task.reminderDate != null) {
                if (!Time.isExpiredReminderDate(task.reminderDate!!)) {
                    if (!isFinished) {
                        scheduleNotification(
                            getString(R.string.TitleReminder),
                            getString(R.string.MessageReminder, task.name),
                            task.id,
                            task.reminderDate!!
                        )
                    }
                }
            }
        }
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(
        title: String,
        message: String,
        notificationId: Int,
        dateNotification: Date
    ) {
        val intent = Intent(applicationContext, AlarmNotification::class.java)
        intent.putExtra(AlarmNotification.TITLE_EXTRA, title)
        intent.putExtra(AlarmNotification.MESSAGE_EXTRA, message)
        intent.putExtra(AlarmNotification.NOTIFICATION_ID_EXTRA, notificationId)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationId,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val trigger: Long = Time.toTimeInMillis(dateNotification)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            trigger,
            pendingIntent
        )
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun cancelNotification(notificationID: Int) {
        val intent = Intent(applicationContext, AlarmNotification::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(pendingIntent)
    }
}