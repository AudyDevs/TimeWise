package com.example.timewise.domain

import com.example.timewise.domain.model.LabelModel

interface LabelRepository {
    suspend fun getLabels(): List<LabelModel>

    suspend fun getLabelId(id: Int): LabelModel

    suspend fun insertLabel(label: LabelModel)

    suspend fun updateLabel(label: LabelModel)

    suspend fun deleteLabel(label: LabelModel)
}