package com.example.timewise.domain.usecase.task

import com.example.timewise.domain.TaskRepository
import javax.inject.Inject

class GetTaskIdUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(id: Int, idLabel: Int) = repository.getTasksId(id, idLabel)
}