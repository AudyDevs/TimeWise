package com.example.timewise.ui.home.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.timewise.R
import com.example.timewise.domain.model.LabelModel

class HomeAdapter(
    private var labelsList: List<LabelModel> = emptyList(),
    private val onItemSelected: (LabelModel) -> Unit
) :
    RecyclerView.Adapter<HomeViewHolder>() {

    fun updateList(list: List<LabelModel>) {
        val diff = HomeDiffUtil(labelsList, list)
        val result = DiffUtil.calculateDiff((diff))
        labelsList = list
        result.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_label, parent, false)
        return HomeViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int = labelsList.size

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        holder.render(labelsList[position], onItemSelected)
    }
}