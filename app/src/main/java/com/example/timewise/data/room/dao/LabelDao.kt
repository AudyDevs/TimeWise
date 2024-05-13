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

    @Query("SELECT * FROM labels ORDER BY id ASC")
    suspend fun getAllLabels(): List<LabelEntity>

    @Query("SELECT * FROM labels WHERE id = :id")
    suspend fun getLabelID(id: Int): LabelEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLabel(label: LabelEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLabel(label: LabelEntity)

    @Delete
    suspend fun deleteLabel(label: LabelEntity)
}