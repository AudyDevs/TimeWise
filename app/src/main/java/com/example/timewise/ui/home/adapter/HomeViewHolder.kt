package com.example.timewise.ui.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.timewise.databinding.ItemLabelBinding
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.ui.dialog.DialogLabel.Companion.INT_NULL

class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemLabelBinding.bind(view)
    fun render(label: LabelModel, onItemSelected: (LabelModel) -> Unit) {
        binding.tvId.text = label.id.toString()
        binding.tvTitle.text = label.name

        if (label.numberIncomplete > 0) {
            binding.tvNumberIncomplete.text = label.numberIncomplete.toString()
        } else {
            binding.tvNumberIncomplete.text = null
        }

        if (label.image != INT_NULL) {
            binding.image.setImageResource(label.image)
        } else {
            binding.image.setImageDrawable(null)
        }

        if (label.color != INT_NULL) {
            binding.cardColor.setCardBackgroundColor(label.color)
        } else {
            binding.cardColor.setCardBackgroundColor(null)
        }

        binding.cardColor.setOnClickListener { onItemSelected(label) }
    }
}