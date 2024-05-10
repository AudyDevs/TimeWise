package com.example.timewise.domain

import com.example.timewise.domain.model.LabelModel

interface LabelRepository {
    suspend fun getLabels(): List<LabelModel>

    suspend fun insertLabel(label: LabelModel)
}