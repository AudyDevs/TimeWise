package com.example.timewise.domain

import com.example.timewise.domain.model.TaskModel

interface TaskRepository {
    suspend fun getTasks(idLabel: Int): List<TaskModel>

    suspend fun getTasksId(id: Int, idLabel: Int): TaskModel

    suspend fun insertTask(task: TaskModel)

    suspend fun updateTask(task: TaskModel)

    suspend fun updateTaskFinished(id: Int, idLabel: Int, isFinished: Boolean)

    suspend fun updateTaskFavourite(id: Int, idLabel: Int, isFavourite: Boolean)

    suspend fun deleteTask(task: TaskModel)
}