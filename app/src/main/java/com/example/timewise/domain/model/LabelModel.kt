package com.example.timewise.domain.model

import com.example.timewise.R

sealed class LabelModel(val id: Int, val image: Int, val name: String, val numberIncomplete: Int, val color: Int) {
    data object Proyectos : LabelModel(1, R.drawable.ic_calendar_week, "Proyectos", 100, R.color.textColorBlue)
    data object Compra : LabelModel(2, R.drawable.ic_calendar_expired, "Compra", 30, R.color.textColorRed)
    data object Juegos : LabelModel(3, R.drawable.ic_calendar_later, "Juegos", 0, R.color.textColorPurple)
}