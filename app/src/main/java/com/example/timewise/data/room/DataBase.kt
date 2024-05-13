package com.example.timewise.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.timewise.data.room.dao.LabelDao
import com.example.timewise.data.room.dao.TaskDao
import com.example.timewise.data.room.entities.LabelEntity
import com.example.timewise.data.room.entities.TaskEntity

@Database(
    entities = [LabelEntity::class, TaskEntity::class],
    version = 1
)
abstract class DataBase : RoomDatabase() {

    abstract fun loadLabelDao(): LabelDao

    abstract fun loadTaskDao(): TaskDao
}