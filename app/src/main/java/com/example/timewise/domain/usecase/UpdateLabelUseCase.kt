package com.example.timewise.domain.usecase

import com.example.timewise.domain.LabelRepository
import com.example.timewise.domain.model.LabelModel
import javax.inject.Inject

class UpdateLabelUseCase @Inject constructor(private val repository: LabelRepository) {
    suspend operator fun invoke(label: LabelModel) = repository.updateLabel(label)
}