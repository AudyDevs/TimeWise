package com.example.timewise.core.extensions.map.label

import com.example.timewise.data.room.entities.LabelEntity
import com.example.timewise.domain.model.LabelModel

fun LabelModel.toRoom() = LabelEntity(
    id = id,
    image = image,
    name = name,
    numberIncomplete = numberIncomplete,
    color = color,
    backcolor = backcolor
)

fun LabelEntity.toDomain() = LabelModel(
    id = id,
    image = image,
    name = name,
    numberIncomplete = numberIncomplete,
    color = color,
    backcolor = backcolor
)