package com.example.timewise.domain.usecase.task

import com.example.timewise.domain.TaskRepository
import javax.inject.Inject

class UpdateTaskFinishedUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(id: Int, idLabel: Int, isFinished: Boolean) =
        repository.updateTaskFinished(id, idLabel, isFinished)
}