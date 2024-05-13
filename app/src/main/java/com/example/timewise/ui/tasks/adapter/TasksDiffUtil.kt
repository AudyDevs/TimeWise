package com.example.timewise.ui.tasks.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.timewise.domain.model.TaskModel

class TasksDiffUtil(
    private val oldList: List<TaskModel>,
    private val newList: List<TaskModel>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}