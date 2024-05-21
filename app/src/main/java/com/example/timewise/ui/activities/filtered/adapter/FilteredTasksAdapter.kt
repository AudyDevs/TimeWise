package com.example.timewise.ui.activities.filtered.adapter

import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.timewise.R
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.domain.model.TaskModel

class FilteredTasksAdapter(
    private var tasksList: List<TaskModel> = emptyList(),
    private var labelList: List<LabelModel> = emptyList(),
    private val onItemSelected: (TaskModel) -> Unit,
    private val onUpdateFinished: (Int, Boolean) -> Unit,
    private val onUpdateFavourite: (Int, Boolean) -> Unit
) : RecyclerView.Adapter<FilteredTasksViewHolder>() {

    fun updateList(list: List<TaskModel>) {
        val diff = FilteredTasksDiffUtil(tasksList, list)
        val result = DiffUtil.calculateDiff((diff))
        tasksList = list
        result.dispatchUpdatesTo(this)
    }

    fun updateListLabel(list: List<LabelModel>) {
        labelList = list
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FilteredTasksViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_task_filtered, parent, false)
        return FilteredTasksViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int = tasksList.size

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: FilteredTasksViewHolder, position: Int) {
        holder.render(
            tasksList[position],
            labelList,
            onItemSelected,
            onUpdateFinished,
            onUpdateFavourite
        )
    }
}