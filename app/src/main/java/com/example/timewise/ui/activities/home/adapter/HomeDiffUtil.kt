package com.example.timewise.ui.activities.home.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.timewise.domain.model.LabelModel

class HomeDiffUtil(
    private val oldList: List<LabelModel>,
    private val newList: List<LabelModel>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition].id == newList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldList[oldItemPosition] == newList[newItemPosition]
    }
}