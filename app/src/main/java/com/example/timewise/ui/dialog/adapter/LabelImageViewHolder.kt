package com.example.timewise.ui.dialog.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.timewise.databinding.ItemLabelImageBinding
import com.example.timewise.domain.model.ImageModel

class LabelImageViewHolder(view: View) : ViewHolder(view) {

    private val binding = ItemLabelImageBinding.bind(view)
    fun render(image: ImageModel, onItemSelected: (ImageModel) -> Unit) {
        binding.itemImage.setImageResource(image.image)
        binding.itemImage.setOnClickListener {
            onItemSelected(image)
        }
    }
}