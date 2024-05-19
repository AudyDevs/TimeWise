package com.example.timewise.domain.usecase.task

import com.example.timewise.domain.TaskRepository
import javax.inject.Inject

class DeleteAllTasksUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(idLabel: Int) = repository.deleteAllTasks(idLabel)
}