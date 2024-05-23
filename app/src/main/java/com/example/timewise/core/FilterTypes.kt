package com.example.timewise.core

sealed class FilterTypes(val type: String, val title: String) {
    data object Today : FilterTypes("Today", "Tareas para hoy")
    data object Week : FilterTypes("Week", "Tareas para esta semana")
    data object Later : FilterTypes("Later", "Tareas para más tarde")
    data object Expired : FilterTypes("Expired", "Tareas expiradas")
}