package com.example.timewise.domain.usecase.label

import com.example.timewise.domain.LabelRepository
import com.example.timewise.domain.model.LabelModel
import javax.inject.Inject

class DeleteLabelUseCase @Inject constructor(private val repository: LabelRepository) {
    suspend operator fun invoke(label: LabelModel) = repository.deleteLabel(label)
}