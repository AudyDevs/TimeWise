package com.example.timewise.ui.activities.search.view

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
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.timewise.R
import com.example.timewise.core.AlarmNotification
import com.example.timewise.core.Time
import com.example.timewise.core.extensions.showKeyboard
import com.example.timewise.databinding.ActivitySearchBinding
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.model.TaskModel
import com.example.timewise.ui.activities.detail.view.DetailTaskActivity
import com.example.timewise.ui.activities.search.adapter.SearchAdapter
import com.example.timewise.ui.activities.search.viewmodel.SearchViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {

    private val searchedViewModel by viewModels<SearchViewModel>()
    private lateinit var searchedAdapter: SearchAdapter
    private lateinit var binding: ActivitySearchBinding
    private lateinit var labelModel: List<LabelModel>
    private lateinit var taskModel: List<TaskModel>

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initAdapter() {
        searchedAdapter = SearchAdapter(
            onItemSelected = { tasksModel ->
                navigateToDetailTaskActivity(tasksModel.id)
            },
            onUpdateFinished = { id, isFinished ->
                notificationTaskFinished(id, isFinished)
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
                searchedViewModel.tasks.collect {
                    taskModel = it
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