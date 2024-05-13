package com.example.timewise.domain.usecase.label

import com.example.timewise.domain.LabelRepository
import javax.inject.Inject

class GetLabelsUseCase @Inject constructor(private val repository: LabelRepository) {
    suspend operator fun invoke() = repository.getLabels()
}