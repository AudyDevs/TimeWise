package com.example.timewise.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.timewise.data.room.dao.LabelDao
import com.example.timewise.data.room.entities.LabelEntity

@Database(
    entities = [LabelEntity::class],
    version = 1
)
abstract class DataBase : RoomDatabase() {

    abstract fun loadLabelDao(): LabelDao
}