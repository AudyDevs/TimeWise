package com.example.timewise.domain.usecase.filteredtasks

import com.example.timewise.domain.TaskRepository
import javax.inject.Inject

class GetFilteredTasksUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(filterTypes: String) = repository.getFilteredTasks(filterTypes)
}