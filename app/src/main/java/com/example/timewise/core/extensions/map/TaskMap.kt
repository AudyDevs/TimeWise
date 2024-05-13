package com.example.timewise.core.extensions.map

import com.example.timewise.data.room.entities.TaskEntity
import com.example.timewise.domain.model.TaskModel

fun TaskModel.toRoom() = TaskEntity(
    id = id,
    idLabel = idLabel,
    name = name,
    isFinished = isFinished,
    dateFinished = dateFinished,
    isFavourite = isFavourite,
    details = details,
    dateCreation = dateCreation
)

fun TaskEntity.toDomain() = TaskModel(
    id = id,
    idLabel = idLabel,
    name = name,
    isFinished = isFinished,
    dateFinished = dateFinished,
    isFavourite = isFavourite,
    details = details,
    dateCreation = dateCreation
)