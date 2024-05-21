package com.example.timewise.ui.activities.tasks.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.example.timewise.R
import com.example.timewise.core.Time
import com.example.timewise.databinding.ItemTaskBinding
import com.example.timewise.domain.model.TaskModel

class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTaskBinding.bind(view)

    @RequiresApi(Build.VERSION_CODES.O)
    fun render(
        tasksModel: TaskModel,
        textColor: Int,
        onItemSelected: (TaskModel) -> Unit,
        onUpdateFinished: (Int, Boolean) -> Unit,
        onUpdateFavourite: (Int, Boolean) -> Unit
    ) {
        binding.apply {
            tvId.text = tasksModel.id.toString()
            tvName.text = tasksModel.name
            imageFinished.imageTintList = ColorStateList.valueOf(textColor)
            imageFavorite.imageTintList = ColorStateList.valueOf(textColor)
            if (tasksModel.isFinished) {
                imageFinished.setImageResource(R.drawable.ic_circle_checked)
                imageFinished.tag = R.drawable.ic_circle_checked
                tvName.apply {
                    paintFlags = paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                }
            } else {
                imageFinished.setImageResource(R.drawable.ic_circle_unchecked)
                imageFinished.tag = R.drawable.ic_circle_unchecked
                tvName.apply {
                    paintFlags = paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
                }
            }
            if (tasksModel.isFavourite) {
                imageFavorite.setImageResource(R.drawable.ic_star_checked)
                imageFavorite.tag = R.drawable.ic_star_checked
            } else {
                imageFavorite.setImageResource(R.drawable.ic_star_unchecked)
                imageFavorite.tag = R.drawable.ic_star_unchecked
            }
            if (tasksModel.expirationDate != null) {
                layoutExpiration.isVisible = true
                tvExpiration.text = Time.toStringShortExpirationDate(tasksModel.expirationDate!!)

                val currentDate = Time.minusDaysDate(Time.currentDate(), 1)
                if (tasksModel.expirationDate!!.before(currentDate)) {
                    imageExpiration.imageTintList = ColorStateList.valueOf(Color.RED)
                    tvExpiration.setTextColor(Color.RED)
                } else {
                    imageExpiration.imageTintList = ColorStateList.valueOf(Color.GRAY)
                    tvExpiration.setTextColor(Color.GRAY)
                }
            } else {
                layoutExpiration.isVisible = false
                tvExpiration.text = ""
            }
            if (tasksModel.reminderDate != null) {
                if (tasksModel.isFinished) {
                    layoutReminder.isVisible = false
                    tvReminder.text = ""
                } else {
                    layoutReminder.isVisible = true
                    tvReminder.text = Time.toStringShortReminderDate(tasksModel.reminderDate!!)
                }
            } else {
                layoutReminder.isVisible = false
                tvReminder.text = ""
            }

            layoutCard.setOnClickListener { onItemSelected(tasksModel) }
            layoutFinished.setOnClickListener {
                changeCheckFinished()
                onUpdateFinished(
                    tasksModel.id,
                    (imageFinished.tag == R.drawable.ic_circle_checked)
                )
            }
            layoutFavorite.setOnClickListener {
                changeCheckFavourite()
                onUpdateFavourite(
                    tasksModel.id,
                    (imageFavorite.tag == R.drawable.ic_star_checked)
                )
            }
        }
    }

    private fun changeCheckFinished() {
        binding.apply {
            if (imageFinished.tag == R.drawable.ic_circle_checked) {
                imageFinished.setImageResource(R.drawable.ic_circle_unchecked)
                imageFinished.tag = R.drawable.ic_circle_unchecked
            } else {
                imageFinished.setImageResource(R.drawable.ic_circle_checked)
                imageFinished.tag = R.drawable.ic_circle_checked
            }
        }
    }

    private fun changeCheckFavourite() {
        binding.apply {
            if (binding.imageFavorite.tag == R.drawable.ic_star_checked) {
                imageFavorite.setImageResource(R.drawable.ic_star_unchecked)
                imageFavorite.tag = R.drawable.ic_star_unchecked
            } else {
                imageFavorite.setImageResource(R.drawable.ic_star_checked)
                imageFavorite.tag = R.drawable.ic_star_checked
            }
        }
    }
}