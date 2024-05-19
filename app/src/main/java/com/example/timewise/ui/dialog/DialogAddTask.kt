package com.example.timewise.ui.dialog

import android.app.Dialog
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import com.example.timewise.R
import com.example.timewise.core.Time
import com.example.timewise.core.extensions.showKeyboard
import com.example.timewise.databinding.DialogAddTaskBinding
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.model.TaskModel
import com.example.timewise.ui.activities.tasks.view.TasksActivity

class DialogAddTask(
    context: TasksActivity,
    private val labelModel: LabelModel,
    private var onClickButtonAdd: (TaskModel) -> Unit
) : Dialog(context) {

    private val binding = DialogAddTaskBinding.inflate(layoutInflater)

    init {
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCancelable(true)
        setContentView(binding.root)
        window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window?.setGravity(Gravity.BOTTOM)
        window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        initListeners()
        changeColorUI()
        initDialogValues()
        show()
    }

    private fun initListeners() {
        binding.apply {
            btnCheck.setOnClickListener {
                if (btnCheck.tag == R.drawable.ic_circle_checked) {
                    btnCheck.setImageResource(R.drawable.ic_circle_unchecked)
                    btnCheck.tag = R.drawable.ic_circle_unchecked
                } else {
                    btnCheck.setImageResource(R.drawable.ic_circle_checked)
                    btnCheck.tag = R.drawable.ic_circle_checked
                }
            }
            btnAdd.setOnClickListener {
                saveModelValues()
            }

            etName.setOnEditorActionListener { _, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    saveModelValues()
                    return@setOnEditorActionListener true
                }
                false
            }
        }
    }

    private fun saveModelValues() {
        if (binding.etName.text.isNotEmpty()) {
            val isFinished: Boolean = binding.btnCheck.tag == R.drawable.ic_circle_checked
            val finishedDate = if (isFinished) {
                Time.currentDate()
            } else {
                null
            }
            val taskModel = TaskModel(
                id = 0,
                idLabel = labelModel.id,
                name = binding.etName.text.toString(),
                isFinished = isFinished,
                finishedDate = finishedDate,
                isFavourite = false,
                reminderDate = null,
                expirationDate = null,
                details = "",
                creationDate = Time.currentDate()
            )
            onClickButtonAdd(taskModel)
            resetValues()
        }
    }

    private fun resetValues() {
        binding.etName.setText("")
        binding.btnCheck.setImageResource(R.drawable.ic_circle_unchecked)
    }

    private fun changeColorUI() {
        binding.apply {
            btnCheck.imageTintList = ColorStateList.valueOf(labelModel.textColor)
            btnAdd.imageTintList = ColorStateList.valueOf(labelModel.textColor)
            etName.backgroundTintList = ColorStateList.valueOf(labelModel.textColor)
        }
    }

    private fun initDialogValues() {
        binding.apply {
            etName.requestFocus()
            etName.postDelayed({
                etName.context.showKeyboard(etName)
            }, 0)
        }
    }
}