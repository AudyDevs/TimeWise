package com.example.timewise.ui.home.adapter

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.timewise.databinding.ItemLabelBinding
import com.example.timewise.domain.model.LabelModel

class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {

    private val binding = ItemLabelBinding.bind(view)
    fun render(label: LabelModel, onItemSelected: (LabelModel) -> Unit) {
        val context = binding.cardColor.context
        val cardColor = ContextCompat.getColor(context, label.color)

        binding.image.setImageResource(label.image)
        binding.tvTitle.text = label.name
        binding.tvNumberIncomplete.text = label.numberIncomplete.toString()
        binding.cardColor.setCardBackgroundColor(cardColor)

        binding.cardColor.setOnClickListener { onItemSelected(label) }
    }
}