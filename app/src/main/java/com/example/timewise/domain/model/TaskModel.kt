package com.example.timewise.domain.model

data class TaskModel(
    val id: Int,
    val idLabel: Int,
    val name: String,
    var isFinished: Boolean,
    val dateFinished: String,
    var isFavourite: Boolean,
    val details: String,
    val dateCreation: String
)