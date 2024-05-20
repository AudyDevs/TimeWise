package com.example.timewise.domain.usecase.search

import com.example.timewise.domain.TaskRepository
import javax.inject.Inject

class GetSearchedTasksUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(search: String) = repository.getSearchedTasks(search)
}