package com.example.timewise.core

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

object Time {
    @SuppressLint("SimpleDateFormat")
    fun currentDate(): Date = Date()

    @RequiresApi(Build.VERSION_CODES.O)
    fun setHourDate(date: Date, setHour: Int): Date {
        var localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        localDate = localDate.withHour(setHour)
        localDate = localDate.withMinute(0)
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun plusHoursDate(date: Date, plusHours: Long): Date {
        var localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        localDate = localDate.plusHours(plusHours)
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun plusDaysDate(date: Date, plusDays: Long): Date {
        var localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        localDate = localDate.plusDays(plusDays)
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun minusDaysDate(date: Date, minusDays: Long): Date {
        var localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        localDate = localDate.minusDays(minusDays)
        return Date.from(localDate.atZone(ZoneId.systemDefault()).toInstant())
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toStringReminderDate(date: Date): String {
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        return localDate.format(
            DateTimeFormatter.ofPattern(
                "'Avisame el' dd MMM yyyy 'a la' HH:mm",
                Locale("es")
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toStringShortReminderDate(date: Date): String {
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        return localDate.format(
            DateTimeFormatter.ofPattern(
                "dd MMM yyyy 'a la' HH:mm",
                Locale("es")
            )
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toStringExpirationDate(date: Date): String {
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate.format(DateTimeFormatter.ofPattern("'Vence el' dd MMM yyyy", Locale("es")))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toStringShortExpirationDate(date: Date): String {
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("es")))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toStringCreateDate(date: Date): String {
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate()
        return localDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy", Locale("es")))
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isCurrentDate(date: Date): Boolean {
        val firstLocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val currentLocalDate =
            currentDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val formatDate = firstLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val formatCurrentDate = currentLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        return formatCurrentDate.compareTo(formatDate) == 0
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isWeekDate(date: Date): Boolean {
        val initialWeekDate = currentDate()
        val finalWeekDate = plusDaysDate(currentDate(), 8)

        val initialWeekLocalDate =
            initialWeekDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val finalWeekLocalDate =
            finalWeekDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        val formatWeekInitial =
            initialWeekLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val formatWeekFinal = finalWeekLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val formatDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        return (formatWeekInitial < formatDate) and (formatDate < formatWeekFinal)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isLaterDate(date: Date): Boolean {
        val initialDate = plusDaysDate(currentDate(), 7)

        val initialLocalDate =
            initialDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        val formatInitial =
            initialLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val formatDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        return (formatInitial < formatDate)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun isExpiredDate(date: Date): Boolean {
        val currentDate = currentDate()

        val currentLocalDate =
            currentDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()
        val localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime()

        val formatCurrent =
            currentLocalDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        val formatDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))

        return (formatDate < formatCurrent)
    }

    fun toTimeInMillis(date: Date): Long {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar.timeInMillis
    }
}
