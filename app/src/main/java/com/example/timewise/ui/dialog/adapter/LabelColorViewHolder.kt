package com.example.timewise.ui.dialog.adapter

import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.timewise.databinding.ItemLabelColorBinding
import com.example.timewise.domain.model.ColorModel

class LabelColorViewHolder(view: View) : ViewHolder(view) {

    private val binding = ItemLabelColorBinding.bind(view)
    fun render(color: ColorModel, isSamePosition: Boolean) {
        val context = binding.itemColor.context
        val backcolor = ContextCompat.getColor(context, color.backcolor)
        val textColor = ContextCompat.getColor(context, color.textColor)
        binding.itemColor.setCardBackgroundColor(backcolor)
        binding.imageCheck.imageTintList = ColorStateList.valueOf(textColor)
        binding.imageCheck.isVisible = isSamePosition
    }
}