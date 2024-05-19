package com.example.timewise.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.timewise.data.room.converter.DateConverter
import java.util.Date

@Entity(tableName = "tasks")
@TypeConverters(DateConverter::class)
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "idLabel") val idLabel: Int = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "isFinished") val isFinished: Boolean = false,
    @ColumnInfo(name = "finishedDate") val finishedDate: Date? = null,
    @ColumnInfo(name = "isFavourite") val isFavourite: Boolean = false,
    @ColumnInfo(name = "reminderDate") val reminderDate: Date? = null,
    @ColumnInfo(name = "expirationDate") val expirationDate: Date? = null,
    @ColumnInfo(name = "details") val details: String = "",
    @ColumnInfo(name = "creationDate") val creationDate: Date? = null
)