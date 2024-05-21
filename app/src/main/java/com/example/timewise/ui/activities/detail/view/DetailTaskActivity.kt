package com.example.timewise.ui.activities.detail.view

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.timewise.R
import com.example.timewise.core.AlarmNotification
import com.example.timewise.core.AlarmNotification.Companion.MESSAGE_EXTRA
import com.example.timewise.core.AlarmNotification.Companion.NOTIFICATION_ID_EXTRA
import com.example.timewise.core.AlarmNotification.Companion.TITLE_EXTRA
import com.example.timewise.core.Time
import com.example.timewise.databinding.ActivityDetailTaskBinding
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.model.TaskModel
import com.example.timewise.ui.activities.detail.viewmodel.DetailTaskViewModel
import com.example.timewise.ui.dialog.DialogDelete
import com.example.timewise.ui.menu.ExpiredMenu
import com.example.timewise.ui.menu.ReminderMenu
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class DetailTaskActivity : AppCompatActivity() {

    private val detailTaskViewModel by viewModels<DetailTaskViewModel>()
    private lateinit var binding: ActivityDetailTaskBinding
    private var idTask: Int = 0
    private lateinit var taskModel: TaskModel
    private lateinit var labelModel: LabelModel

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityDetailTaskBinding.inflate(layoutInflater)
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
        initListeners()
        initUIState()
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    private fun initViewModel() {
        detailTaskViewModel.getTaskID(idTask)
    }

    private fun initIntents() {
        idTask = intent?.extras?.getInt("idTask") ?: 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initListeners() {
        binding.apply {
            btnBack.setOnClickListener { backPressed() }
            btnDelete.setOnClickListener { showDialogDelete() }
            btnFinished.setOnClickListener { checkFinished() }
            btnFavourite.setOnClickListener { checkFavourite() }
            etExpirationDate.setOnClickListener { openExpirationMenu() }
            etReminderDate.setOnClickListener { openReminderMenu() }
            layoutExpirationDate.setEndIconOnClickListener { resetExpiredDate() }
            layoutReminderDate.setEndIconOnClickListener { resetReminderDate() }
            fbAdd.setOnClickListener { backPressed() }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailTaskViewModel.task.collect {
                    if (it != null) {
                        taskModel = it
                        detailTaskViewModel.getLabelID(taskModel.idLabel)
                    }
                }
            }
        }
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                detailTaskViewModel.label.collect {
                    if (it != null) {
                        labelModel = it
                        changeUIValues()
                        changeUIColor()
                    }
                }
            }
        }
    }

    private fun changeUIColor() {
        binding.apply {
            toolbar.setBackgroundColor(labelModel.backcolor)
            etNameLabel.setTextColor(labelModel.textColor)
            btnDelete.imageTintList = ColorStateList.valueOf(labelModel.textColor)
            btnDelete.setBackgroundColor(labelModel.backcolor)
            btnBack.imageTintList = ColorStateList.valueOf(labelModel.textColor)
            btnBack.setBackgroundColor(labelModel.backcolor)
            separator.setBackgroundColor(labelModel.textColor)
            layoutNameTask.setBackgroundColor(labelModel.backcolor)
            btnFavourite.imageTintList = ColorStateList.valueOf(labelModel.textColor)
            btnFavourite.setBackgroundColor(labelModel.backcolor)
            btnFinished.imageTintList = ColorStateList.valueOf(labelModel.textColor)
            btnFinished.setBackgroundColor(labelModel.backcolor)
            etNameTask.setBackgroundColor(labelModel.backcolor)
            etNameTask.setTextColor(labelModel.textColor)
            layoutExpirationDate.setEndIconTintList(ColorStateList.valueOf(labelModel.textColor))
            layoutExpirationDate.setStartIconTintList(ColorStateList.valueOf(labelModel.textColor))
            layoutReminderDate.setEndIconTintList(ColorStateList.valueOf(labelModel.textColor))
            layoutReminderDate.setStartIconTintList(ColorStateList.valueOf(labelModel.textColor))
            layoutCreateDate.setBackgroundColor(labelModel.backcolor)
            etCreateDate.setTextColor(labelModel.textColor)
            fbAdd.imageTintList = ColorStateList.valueOf(labelModel.backcolor)
            fbAdd.backgroundTintList = ColorStateList.valueOf(labelModel.textColor)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun changeUIValues() {
        binding.apply {
            etNameLabel.text = labelModel.name
            etNameTask.text = taskModel.name
            if (taskModel.expirationDate != null) {
                etExpirationDate.setText(Time.toStringExpirationDate(taskModel.expirationDate!!))
                etExpirationDate.tag = taskModel.expirationDate
                changeColorExpiration(taskModel.expirationDate!!)
            }
            if (taskModel.reminderDate != null) {
                etReminderDate.setText(Time.toStringReminderDate(taskModel.reminderDate!!))
                etReminderDate.tag = taskModel.reminderDate
            }
            etDetails.setText(taskModel.details)

            etCreateDate.text = ""
            etCreateDate.tag = null
            if (taskModel.isFinished) {
                btnFinished.setImageResource(R.drawable.ic_circle_checked)
                btnFinished.tag = R.drawable.ic_circle_checked
                layoutReminderDate.startIconDrawable =
                    ContextCompat.getDrawable(this@DetailTaskActivity, R.drawable.ic_alarm_disable)
                if (taskModel.finishedDate != null) {
                    etCreateDate.text = getString(
                        R.string.finishedDate,
                        Time.toStringCreateDate(taskModel.finishedDate!!)
                    )
                    etCreateDate.tag = taskModel.finishedDate
                }
            } else {
                btnFinished.setImageResource(R.drawable.ic_circle_unchecked)
                btnFinished.tag = R.drawable.ic_circle_unchecked
                layoutReminderDate.startIconDrawable =
                    ContextCompat.getDrawable(this@DetailTaskActivity, R.drawable.ic_alarm_enable)
                if (taskModel.creationDate != null) {
                    etCreateDate.text = getString(
                        R.string.creationDate,
                        Time.toStringCreateDate(taskModel.creationDate!!)
                    )
                    etCreateDate.tag = taskModel.creationDate
                }
            }
            if (taskModel.isFavourite) {
                btnFavourite.setImageResource(R.drawable.ic_star_checked)
                btnFavourite.tag = R.drawable.ic_star_checked
            } else {
                btnFavourite.setImageResource(R.drawable.ic_star_unchecked)
                btnFavourite.tag = R.drawable.ic_star_unchecked
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun changeColorExpiration(date: Date?) {
        if (date != null) {
            val currentDate = Time.minusDaysDate(Time.currentDate(), 1)
            if (date.before(currentDate)) {
                binding.etExpirationDate.setTextColor(Color.RED)
                binding.layoutExpirationDate.setEndIconTintList(ColorStateList.valueOf(Color.RED))
                binding.layoutExpirationDate.setStartIconTintList(ColorStateList.valueOf(Color.RED))
            } else {
                binding.etExpirationDate.setTextColor(Color.BLACK)
                binding.layoutExpirationDate.setEndIconTintList(ColorStateList.valueOf(labelModel.textColor))
                binding.layoutExpirationDate.setStartIconTintList(ColorStateList.valueOf(labelModel.textColor))
            }
        } else {
            binding.etExpirationDate.setTextColor(Color.BLACK)
            binding.layoutExpirationDate.setEndIconTintList(ColorStateList.valueOf(labelModel.textColor))
            binding.layoutExpirationDate.setStartIconTintList(ColorStateList.valueOf(labelModel.textColor))
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkFinished() {
        binding.apply {
            if (btnFinished.tag == R.drawable.ic_circle_checked) {
                btnFinished.setImageResource(R.drawable.ic_circle_unchecked)
                btnFinished.tag = R.drawable.ic_circle_unchecked
            } else {
                btnFinished.setImageResource(R.drawable.ic_circle_checked)
                btnFinished.tag = R.drawable.ic_circle_checked
            }
        }
        updateTask()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkFavourite() {
        binding.apply {
            if (btnFavourite.tag == R.drawable.ic_star_checked) {
                btnFavourite.setImageResource(R.drawable.ic_star_unchecked)
                btnFavourite.tag = R.drawable.ic_star_unchecked
            } else {
                btnFavourite.setImageResource(R.drawable.ic_star_checked)
                btnFavourite.tag = R.drawable.ic_star_checked
            }
        }
        updateTask()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun backPressed() {
        updateTask()
        onBackPressedDispatcher.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateTask() {
        val finishedDate: Date? = if (binding.btnFinished.tag == R.drawable.ic_circle_checked) {
            if (taskModel.finishedDate == null) {
                Time.currentDate()
            } else {
                taskModel.finishedDate
            }
        } else {
            null
        }

        cancelNotification(taskModel.id)
        if (binding.etReminderDate.tag != null) {
            if (!Time.isExpiredReminderDate(binding.etReminderDate.tag as Date)) {
                if (binding.btnFinished.tag == R.drawable.ic_circle_unchecked) {
                    scheduleNotification(
                        getString(R.string.TitleReminder),
                        getString(R.string.MessageReminder, taskModel.name),
                        taskModel.id,
                        binding.etReminderDate.tag as Date
                    )
                }
            }
        }

        detailTaskViewModel.updateTask(
            TaskModel(
                id = taskModel.id,
                idLabel = taskModel.idLabel,
                name = binding.etNameTask.text.toString(),
                isFinished = (binding.btnFinished.tag == R.drawable.ic_circle_checked),
                finishedDate = finishedDate,
                isFavourite = (binding.btnFavourite.tag == R.drawable.ic_star_checked),
                reminderDate = binding.etReminderDate.tag as Date?,
                expirationDate = binding.etExpirationDate.tag as Date?,
                details = binding.etDetails.text.toString(),
                creationDate = taskModel.creationDate
            )
        )
    }

    private fun showDialogDelete() {
        DialogDelete(
            binding.main,
            R.string.dialogDeleteTaskMessage,
            onSelectedButton = { isSelected ->
                if (isSelected) {
                    deleteTask()
                }
            })
    }

    private fun deleteTask() {
        detailTaskViewModel.deleteTask(taskModel)
        onBackPressedDispatcher.onBackPressed()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun resetExpiredDate() {
        binding.etExpirationDate.setText("")
        binding.etExpirationDate.tag = null
        changeColorExpiration(null)
    }

    private fun resetReminderDate() {
        binding.etReminderDate.setText("")
        binding.etReminderDate.tag = null
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openExpirationMenu() {
        ExpiredMenu(binding.menuExpiration, onSelectedMenu = { date ->
            binding.etExpirationDate.setText(Time.toStringExpirationDate(date))
            binding.etExpirationDate.tag = date
            changeColorExpiration(date)
        })
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun openReminderMenu() {
        ReminderMenu(binding.menuReminder, onSelectedMenu = { date ->
            binding.etReminderDate.setText(Time.toStringReminderDate(date))
            binding.etReminderDate.tag = date
        })
    }

    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleNotification(
        title: String,
        message: String,
        notificationId: Int,
        dateNotification: Date
    ) {
        val intent = Intent(applicationContext, AlarmNotification::class.java)
        intent.putExtra(TITLE_EXTRA, title)
        intent.putExtra(MESSAGE_EXTRA, message)
        intent.putExtra(NOTIFICATION_ID_EXTRA, notificationId)
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