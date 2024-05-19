package com.example.timewise.ui.activities.home.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.timewise.databinding.ItemLabelBinding
import com.example.timewise.domain.model.LabelModel
import com.example.timewise.ui.dialog.DialogLabel.Companion.INT_NULL

class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemLabelBinding.bind(view)
    fun render(label: LabelModel, onItemSelected: (LabelModel) -> Unit) {
        binding.apply {
            tvId.text = label.id.toString()
            tvTitle.text = label.name

            if (label.image != INT_NULL) {
                image.setImageResource(label.image)
            } else {
                image.setImageDrawable(null)
            }

            if (label.textColor != INT_NULL) {
                cardColor.setCardBackgroundColor(label.textColor)
            } else {
                cardColor.setCardBackgroundColor(null)
            }

            cardColor.setOnClickListener { onItemSelected(label) }
        }
    }
}