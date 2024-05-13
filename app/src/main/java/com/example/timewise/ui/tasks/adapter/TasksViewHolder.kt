package com.example.timewise.ui.tasks.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.timewise.R
import com.example.timewise.databinding.ItemTaskBinding
import com.example.timewise.domain.model.TaskModel

class TasksViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemTaskBinding.bind(view)
    fun render(
        tasksModel: TaskModel,
        onItemSelected: (TaskModel) -> Unit,
        onUpdateFinished: (Int, Boolean) -> Unit,
        onUpdateFavourite: (Int, Boolean) -> Unit
    ) {
        binding.apply {
            tvId.text = tasksModel.id.toString()
            tvName.text = tasksModel.name
            if (tasksModel.isFinished) {
                imageFinished.setImageResource(R.drawable.ic_circle_checked)
                imageFinished.tag = R.drawable.ic_circle_checked
            } else {
                imageFinished.setImageResource(R.drawable.ic_circle_unchecked)
                imageFinished.tag = R.drawable.ic_circle_unchecked
            }
            if (tasksModel.isFavourite) {
                imageFavorite.setImageResource(R.drawable.ic_star_checked)
                imageFavorite.tag = R.drawable.ic_star_checked
            } else {
                imageFavorite.setImageResource(R.drawable.ic_star_unchecked)
                imageFavorite.tag = R.drawable.ic_star_unchecked
            }
            layoutCard.setOnClickListener { onItemSelected(tasksModel) }
            layoutFinished.setOnClickListener {
                changeCheckFinished()
                onUpdateFinished(tasksModel.id, (imageFinished.tag == R.drawable.ic_circle_checked))
            }
            layoutFavorite.setOnClickListener {
                changeCheckFavourite()
                onUpdateFavourite(tasksModel.id, (imageFavorite.tag == R.drawable.ic_star_checked))
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