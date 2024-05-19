package com.example.timewise.domain.model

import java.util.Date

data class TaskModel(
    val id: Int,
    val idLabel: Int,
    var name: String,
    var isFinished: Boolean,
    var finishedDate: Date?,
    var isFavourite: Boolean,
    var reminderDate: Date?,
    var expirationDate: Date?,
    var details: String,
    val creationDate: Date?
)