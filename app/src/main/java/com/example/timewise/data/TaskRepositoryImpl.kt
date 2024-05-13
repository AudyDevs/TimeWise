package com.example.timewise.data

import com.example.timewise.core.extensions.map.toDomain
import com.example.timewise.core.extensions.map.toRoom
import com.example.timewise.data.room.dao.TaskDao
import com.example.timewise.domain.TaskRepository
import com.example.timewise.domain.model.TaskModel
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {
    override suspend fun getTasks(idLabel: Int): List<TaskModel> {
        val response = taskDao.getAllTasks(idLabel)
        return response.map { it.toDomain() }
    }

    override suspend fun getTasksId(id: Int, idLabel: Int): TaskModel {
        val response = taskDao.getTaskID(id, idLabel)
        return response.toDomain()
    }

    override suspend fun insertTask(task: TaskModel) {
        val response = task.toRoom()
        taskDao.insertTask(response)
    }

    override suspend fun updateTask(task: TaskModel) {
        val response = task.toRoom()
        taskDao.updateTask(response)
    }

    override suspend fun updateTaskFinished(id: Int, idLabel: Int, isFinished: Boolean) {
        taskDao.updateTaskFinished(id, idLabel, isFinished)
    }

    override suspend fun updateTaskFavourite(id: Int, idLabel: Int, isFavourite: Boolean) {
        taskDao.updateTaskFavourite(id, idLabel, isFavourite)
    }

    override suspend fun deleteTask(task: TaskModel) {
        val response = task.toRoom()
        taskDao.deleteTask(response)
    }
}