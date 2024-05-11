package com.example.timewise.ui.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timewise.R
import com.example.timewise.domain.model.ImageModel

class LabelImageAdapter(
    private var images: List<ImageModel> = emptyList(),
    private val onItemSelected: (ImageModel) -> Unit
) :
    RecyclerView.Adapter<LabelImageViewHolder>() {

    fun updateList(newImages: List<ImageModel>) {
        images = newImages
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelImageViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_label_image, parent, false)
        return LabelImageViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int = images.size

    override fun onBindViewHolder(holder: LabelImageViewHolder, position: Int) {
        holder.render(images[position], onItemSelected)
    }
}