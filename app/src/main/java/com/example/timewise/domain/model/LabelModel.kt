package com.example.timewise.domain.model

data class LabelModel(
    val id: Int,
    val image: Int,
    val name: String,
    val numberIncomplete: Int,
    val color: Int,
    val backcolor: Int
) {
}