package com.example.timewise.domain.usecase

import com.example.timewise.domain.LabelRepository
import javax.inject.Inject

class GetLabelIdUseCase @Inject constructor(private val repository: LabelRepository) {
    suspend operator fun invoke(id: Int) = repository.getLabelId(id)
}