package com.example.timewise.data.room.converter

import androidx.room.TypeConverter
import java.util.Date

class DateConverter  {
    @TypeConverter
    fun toDate(timestamp: Long?): Date? {
        return timestamp?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}