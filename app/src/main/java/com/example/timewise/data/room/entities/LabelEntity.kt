package com.example.timewise.data.room.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "label")
data class LabelEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "image") val image: Int = 0,
    @ColumnInfo(name = "name") val name: String = "",
    @ColumnInfo(name = "numberIncomplete") val numberIncomplete: Int = 0,
    @ColumnInfo(name = "color") val color: Int = 0,
    @ColumnInfo(name = "backcolor") val backcolor: Int = 0
)