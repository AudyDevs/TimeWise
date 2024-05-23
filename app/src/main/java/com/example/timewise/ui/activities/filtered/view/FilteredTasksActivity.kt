package com.example.timewise.ui.activities.filtered.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
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
import com.example.timewise.core.FilterTypes
import com.example.timewise.core.Time
import com.example.timewise.databinding.ActivityFilteredTasksBinding
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.model.TaskModel
import com.example.timewise.ui.activities.detail.view.DetailTaskActivity
import com.example.timewise.ui.activities.filtered.adapter.FilteredTasksAdapter
import com.example.timewise.ui.activities.filtered.viewmodel.FilteredTasksViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class FilteredTasksActivity : AppCompatActivity() {

    private val filteredTasksViewModel by viewModels<FilteredTasksViewModel>()
    private lateinit var filteredTasksAdapter: FilteredTasksAdapter
    private lateinit var binding: ActivityFilteredTasksBinding
    private var filterType = ""
    private lateinit var labelModel: List<LabelModel>
    private lateinit var taskModel: List<TaskModel>

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUI() {
        initIntent()
        initAdapter()
        initListeners()
        initUIState()
        initValues()
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    private fun initViewModel() {
        filteredTasksViewModel.getLabels()
        filteredTasksViewModel.filterTypes = filterType
        filteredTasksViewModel.getFilteredTasks()
    }

    private fun initIntent() {
        filterType = intent?.extras?.getString("filterType") ?: FilterTypes.Today.type
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initAdapter() {
        filteredTasksAdapter = FilteredTasksAdapter(onItemSelected = { tasksModel ->
            navigateToDetailTaskActivity(tasksModel.id)
        }, onUpdateFinished = { id, isFinished ->
            notificationTaskFinished(id, isFinished)
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
                filteredTasksViewModel.tasks.collect {
                    taskModel = it
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

    private fun initValues() {
        binding.etNameFilter.text = when (filterType) {
            FilterTypes.Today.type -> FilterTypes.Today.title
            FilterTypes.Week.type -> FilterTypes.Week.title
            FilterTypes.Later.type -> FilterTypes.Later.title
            FilterTypes.Expired.type -> FilterTypes.Expired.title
            else -> FilterTypes.Today.title
        }
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

