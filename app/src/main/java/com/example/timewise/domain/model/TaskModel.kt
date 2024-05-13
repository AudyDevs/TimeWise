package com.example.timewise.domain.model

data class TaskModel(
    val id: Int,
    val idLabel: Int,
    val name: String,
    val isFinished: Boolean,
    val dateFinished: String,
    val isFavourite: Boolean,
    val details: String,
    val dateCreation: String
)