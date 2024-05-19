package com.example.timewise.data

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.timewise.core.Time
import com.example.timewise.core.extensions.map.toDomain
import com.example.timewise.core.extensions.map.toRoom
import com.example.timewise.data.room.dao.TaskDao
import com.example.timewise.domain.TaskRepository
import com.example.timewise.domain.model.TaskModel
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(private val taskDao: TaskDao) : TaskRepository {
    override suspend fun getTasks(idLabel: Int): List<TaskModel> {
        val response = taskDao.getLabelTasks(idLabel)
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

    override suspend fun deleteAllTasks(idLabel: Int) {
        taskDao.deleteAllTasks(idLabel)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getNumberTasksToday(): Int {
        var number = 0
        val response = taskDao.getAllTasks()
        val responseFiltered = response.filter { !it.isFinished }
        responseFiltered.forEach { taskEntity ->
            if (taskEntity.expirationDate != null) {
                if (Time.isCurrentDate(taskEntity.expirationDate)) {
                    number += 1
                }
            }
        }
        return number
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getNumberTasksWeek(): Int {
        var number = 0
        val response = taskDao.getAllTasks()
        val responseFiltered = response.filter { !it.isFinished }
        responseFiltered.forEach { taskEntity ->
            if (taskEntity.expirationDate != null) {
                if (Time.isWeekDate(taskEntity.expirationDate)) {
                    number += 1
                }
            }
        }
        return number
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getNumberTasksLater(): Int {
        var number = 0
        val response = taskDao.getAllTasks()
        val responseFiltered = response.filter { !it.isFinished }
        responseFiltered.forEach { taskEntity ->
            if (taskEntity.expirationDate != null) {
                if (Time.isLaterDate(taskEntity.expirationDate)) {
                    number += 1
                }
            }
        }
        return number
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getNumberTasksExpired(): Int {
        var number = 0
        val response = taskDao.getAllTasks()
        val responseFiltered = response.filter { !it.isFinished }
        responseFiltered.forEach { taskEntity ->
            if (taskEntity.expirationDate != null) {
                if (Time.isExpiredDate(taskEntity.expirationDate)) {
                    number += 1
                }
            }
        }
        return number
    }
}