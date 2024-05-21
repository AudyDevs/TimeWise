package com.example.timewise.domain.usecase.task

import com.example.timewise.domain.TaskRepository
import javax.inject.Inject

class UpdateTaskFavouriteUseCase @Inject constructor(private val repository: TaskRepository) {
    suspend operator fun invoke(id: Int, isFavourite: Boolean) =
        repository.updateTaskFavourite(id, isFavourite)
}