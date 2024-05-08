package com.example.timewise.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.timewise.data.room.entities.LabelEntity

@Dao
interface LabelDao {

    @Query("SELECT * FROM label ORDER BY id ASC")
    suspend fun getAllLabels(): List<LabelEntity>

    @Insert
    suspend fun insertLabel(label: List<LabelEntity>)

    @Update
    suspend fun updateLabel(label: LabelEntity)

    @Delete
    suspend fun deleteLabel(label: LabelEntity)

    @Query("DELETE FROM label")
    suspend fun deleteAllLabels()
}