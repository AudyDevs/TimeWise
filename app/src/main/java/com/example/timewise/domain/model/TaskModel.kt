package com.example.timewise.domain.model

import com.example.timewise.R

sealed class TaskModel(val image: Int, val name: String, val numberIncomplete: Int) {
    data object Proyectos : TaskModel(R.drawable.ic_calendar_week, "Proyectos", 100)
    data object Compra : TaskModel(R.drawable.ic_calendar_expired, "Compra", 30)
    data object Juegos : TaskModel(R.drawable.ic_calendar_later, "Juegos", 0)
}