package com.example.timewise.domain.usecase.filteredtasks

import com.example.timewise.core.FilterTypes
import com.example.timewise.domain.TaskRepository
import javax.inject.Inject

class GetNumberFilteredTasksUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(filterTypes: FilterTypes) =
        repository.getNumberFilteredTasks(filterTypes)
}