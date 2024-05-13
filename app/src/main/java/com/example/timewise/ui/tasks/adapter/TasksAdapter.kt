package com.example.timewise.ui.tasks.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.example.timewise.R
import com.example.timewise.domain.model.TaskModel

class TasksAdapter(
    private var tasksList: List<TaskModel> = emptyList(),
    private val onItemSelected: (TaskModel) -> Unit
) : Adapter<TasksViewHolder>() {

    fun updateList(list: List<TaskModel>) {
        val diff = TasksDiffUtil(tasksList, list)
        val result = DiffUtil.calculateDiff((diff))
        tasksList = list
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TasksViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_task, parent, false)
        return TasksViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int = tasksList.size

    override fun onBindViewHolder(holder: TasksViewHolder, position: Int) {
        holder.render(tasksList[position], onItemSelected)
    }
}