package com.example.timewise.ui.detail.view

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
import com.example.timewise.core.Time
import com.example.timewise.databinding.ActivityDetailTaskBinding
import com.example.timewise.domain.model.TaskModel
import com.example.timewise.ui.detail.viewmodel.DetailTaskViewModel
import com.example.timewise.ui.dialog.DialogDelete
import com.example.timewise.ui.dialog.DialogLabel.Companion.INT_NULL
import com.example.timewise.ui.menu.ExpiredMenu
import com.example.timewise.ui.menu.ReminderMenu
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.util.Date

@AndroidEntryPoint
class DetailTaskActivity : AppCompatActivity() {

    private val detailTaskViewModel by viewModels<DetailTaskViewModel>()
    private lateinit var binding: ActivityDetailTaskBinding
    private var id: Int = 0
    private var idLabel: Int = 0
    private var nameLabel: String = ""
    private var textColor: Int = INT_NULL
    private var backcolor: Int = INT_NULL
    private lateinit var taskModel: TaskModel

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
        initUIColor()
        initListeners()
        initUIState()
    }

    override fun onResume() {
        super.onResume()
        initViewModel()
    }

    private fun initViewModel() {
        detailTaskViewModel.getTaskID(id, idLabel)
    }

    private fun initIntents() {
        id = intent?.extras?.getInt("id") ?: 0
        idLabel = intent?.extras?.getInt("idLabel") ?: 0
        nameLabel = intent?.extras?.getString("nameLabel") ?: ""
        textColor = intent?.extras?.getInt("textColor") ?: INT_NULL
        backcolor = intent?.extras?.getInt("backcolor") ?: INT_NULL
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
                        changeUIValues()
                    }
                }
            }
        }
    }

    private fun initUIColor() {
        binding.apply {
            toolbar.setBackgroundColor(backcolor)
            etNameLabel.setTextColor(textColor)
            btnDelete.imageTintList = ColorStateList.valueOf(textColor)
            btnDelete.setBackgroundColor(backcolor)
            btnBack.imageTintList = ColorStateList.valueOf(textColor)
            btnBack.setBackgroundColor(backcolor)
            separator.setBackgroundColor(textColor)
            layoutNameTask.setBackgroundColor(backcolor)
            btnFavourite.imageTintList = ColorStateList.valueOf(textColor)
            btnFavourite.setBackgroundColor(backcolor)
            btnFinished.imageTintList = ColorStateList.valueOf(textColor)
            btnFinished.setBackgroundColor(backcolor)
            etNameTask.setBackgroundColor(backcolor)
            etNameTask.setTextColor(textColor)
            layoutExpirationDate.setEndIconTintList(ColorStateList.valueOf(textColor))
            layoutExpirationDate.setStartIconTintList(ColorStateList.valueOf(textColor))
            layoutReminderDate.setEndIconTintList(ColorStateList.valueOf(textColor))
            layoutReminderDate.setStartIconTintList(ColorStateList.valueOf(textColor))
            layoutCreateDate.setBackgroundColor(backcolor)
            etCreateDate.setTextColor(textColor)
            fbAdd.imageTintList = ColorStateList.valueOf(backcolor)
            fbAdd.backgroundTintList = ColorStateList.valueOf(textColor)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun changeUIValues() {
        binding.apply {
            etNameLabel.text = nameLabel
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
                binding.layoutExpirationDate.setEndIconTintList(ColorStateList.valueOf(textColor))
                binding.layoutExpirationDate.setStartIconTintList(ColorStateList.valueOf(textColor))
            }
        } else {
            binding.etExpirationDate.setTextColor(Color.BLACK)
            binding.layoutExpirationDate.setEndIconTintList(ColorStateList.valueOf(textColor))
            binding.layoutExpirationDate.setStartIconTintList(ColorStateList.valueOf(textColor))
        }
    }

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

    private fun backPressed() {
        updateTask()
        onBackPressedDispatcher.onBackPressed()
    }

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
}