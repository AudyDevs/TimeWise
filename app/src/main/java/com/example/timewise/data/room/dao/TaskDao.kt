package com.example.timewise.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.timewise.data.room.entities.TaskEntity

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    suspend fun getAllTasks(): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE idLabel = :idLabel ORDER BY id ASC")
    suspend fun getLabelTasks(idLabel: Int): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getTaskID(id: Int): TaskEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task: TaskEntity)

    @Query("UPDATE tasks SET isFinished = :isFinished WHERE id = :id")
    suspend fun updateTaskFinished(id: Int, isFinished: Boolean)

    @Query("UPDATE tasks SET isFavourite = :isFavourite WHERE id = :id")
    suspend fun updateTaskFavourite(id: Int, isFavourite: Boolean)

    @Delete
    suspend fun deleteTask(task: TaskEntity)

    @Query("DELETE FROM tasks WHERE idLabel = :idLabel")
    suspend fun deleteAllTasks(idLabel: Int)
}