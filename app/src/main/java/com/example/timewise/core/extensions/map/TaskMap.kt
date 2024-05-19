package com.example.timewise.core.extensions.map

import com.example.timewise.data.room.entities.TaskEntity
import com.example.timewise.domain.model.TaskModel

fun TaskModel.toRoom() = TaskEntity(
    id = id,
    idLabel = idLabel,
    name = name,
    isFinished = isFinished,
    finishedDate = finishedDate,
    isFavourite = isFavourite,
    reminderDate = reminderDate,
    expirationDate = expirationDate,
    details = details,
    creationDate = creationDate
)

fun TaskEntity.toDomain() = TaskModel(
    id = id,
    idLabel = idLabel,
    name = name,
    isFinished = isFinished,
    finishedDate = finishedDate,
    isFavourite = isFavourite,
    reminderDate = reminderDate,
    expirationDate = expirationDate,
    details = details,
    creationDate = creationDate
)