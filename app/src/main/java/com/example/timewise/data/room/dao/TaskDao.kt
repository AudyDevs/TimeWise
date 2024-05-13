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

    @Query("SELECT * FROM tasks WHERE idLabel = :idLabel ORDER BY id ASC")
    suspend fun getAllTasks(idLabel: Int): List<TaskEntity>

    @Query("SELECT * FROM tasks WHERE idLabel = :idLabel AND id = :id")
    suspend fun getTaskID(id: Int, idLabel: Int): TaskEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateTask(task: TaskEntity)

    @Delete
    suspend fun deleteTask(task: TaskEntity)
}