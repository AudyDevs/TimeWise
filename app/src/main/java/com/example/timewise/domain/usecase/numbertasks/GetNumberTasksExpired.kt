package com.example.timewise.domain.usecase.numbertasks

import com.example.timewise.domain.TaskRepository
import javax.inject.Inject

class GetNumberTasksExpired @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke() = repository.getNumberTasksExpired()
}