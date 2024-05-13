package com.example.timewise.ui.tasks.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.timewise.databinding.ItemTaskBinding
import com.example.timewise.domain.model.TaskModel

class TasksViewHolder(view: View): RecyclerView.ViewHolder(view) {

    private val binding = ItemTaskBinding.bind(view)
    fun render(tasksModel: TaskModel, onItemSelected: (TaskModel) -> Unit) {

    }
}