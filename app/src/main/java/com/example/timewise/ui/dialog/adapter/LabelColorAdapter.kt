package com.example.timewise.ui.dialog.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.timewise.R
import com.example.timewise.domain.model.ColorModel

class LabelColorAdapter(
    private var colors: List<ColorModel> = emptyList(),
    private var onItemSelected: (ColorModel) -> Unit
) :
    RecyclerView.Adapter<LabelColorViewHolder>() {

    private var selectedColorPosition = -1

    fun updateList(newColors: List<ColorModel>) {
        colors = newColors
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LabelColorViewHolder {
        val layoutInflater =
            LayoutInflater.from(parent.context).inflate(R.layout.item_label_color, parent, false)
        return LabelColorViewHolder(layoutInflater)
    }

    override fun getItemCount(): Int = colors.size

    override fun onBindViewHolder(holder: LabelColorViewHolder, position: Int) {
        holder.render(colors[position])
        holder.itemView.setOnClickListener {
            selectedColorPosition = holder.adapterPosition
            onItemSelected(colors[position])
            notifyDataSetChanged()
        }
    }
}