package com.example.timewise.data.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.timewise.data.room.entities.LabelEntity

@Dao
interface LabelDao {

    @Query("SELECT * FROM label ORDER BY id ASC")
    suspend fun getAllLabels(): List<LabelEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabel(label: LabelEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLabel(label: LabelEntity)

    @Delete
    suspend fun deleteLabel(label: LabelEntity)

    @Query("DELETE FROM label")
    suspend fun deleteAllLabels()
}