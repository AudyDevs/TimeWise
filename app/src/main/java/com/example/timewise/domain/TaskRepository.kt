package com.example.timewise.domain

import com.example.timewise.domain.model.TaskModel

interface TaskRepository {
    suspend fun getTasks(idLabel: Int): List<TaskModel>

    suspend fun getTasksId(id: Int): TaskModel

    suspend fun insertTask(task: TaskModel)

    suspend fun updateTask(task: TaskModel)

    suspend fun updateTaskFinished(id: Int, isFinished: Boolean)

    suspend fun updateTaskFavourite(id: Int, isFavourite: Boolean)

    suspend fun deleteTask(task: TaskModel)

    suspend fun deleteAllTasks(idLabel: Int)

    suspend fun getNumberFilteredTasks(filterTypes: String): Int

    suspend fun getFilteredTasks(filterTypes: String): List<TaskModel>

    suspend fun getSearchedTasks(search: String): List<TaskModel>
}