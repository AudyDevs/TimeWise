package com.example.timewise.core.extensions.map

import com.example.timewise.data.room.entities.LabelEntity
import com.example.timewise.domain.model.LabelModel

fun LabelModel.toRoom() = LabelEntity(
    id = id,
    image = image,
    name = name,
    textColor = textColor,
    backcolor = backcolor
)

fun LabelEntity.toDomain() = LabelModel(
    id = id,
    image = image,
    name = name,
    textColor = textColor,
    backcolor = backcolor
)