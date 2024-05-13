package com.example.timewise.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "idLabel") val idLabel: Int = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "isFinished") val isFinished: Boolean = false,
    @ColumnInfo(name = "dateFinished") val dateFinished: String = "",
    @ColumnInfo(name = "isFavourite") val isFavourite: Boolean = false,
    @ColumnInfo(name = "details") val details: String = "",
    @ColumnInfo(name = "dateCreation") val dateCreation: String = ""
)