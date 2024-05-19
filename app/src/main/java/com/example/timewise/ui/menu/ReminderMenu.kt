package com.example.timewise.ui.menu

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.DatePicker
import android.widget.PopupMenu
import android.widget.TimePicker
import androidx.annotation.RequiresApi
import com.example.timewise.R
import com.example.timewise.core.Time
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
class ReminderMenu(private val view: View, private val onSelectedMenu: (Date) -> Unit) :
    DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0

    init {
        val wrapper = ContextThemeWrapper(view.context, R.style.PopupMenu)
        val popupMenu = PopupMenu(wrapper, view)
        popupMenu.inflate(R.menu.menu_reminder)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            var date: Date
            when (menuItem.itemId) {
                R.id.menuReminderToday -> {
                    date = Time.plusHoursDate(Time.currentDate(), 3)
                    onSelectedMenu(date)
                    true
                }

                R.id.menuReminderTomorrow -> {
                    date = Time.plusDaysDate(Time.currentDate(), 1)
                    date = Time.setHourDate(date, 9)
                    onSelectedMenu(date)
                    true
                }

                R.id.menuReminderLater -> {
                    date = Time.plusDaysDate(Time.currentDate(), 7)
                    date = Time.setHourDate(date, 9)
                    onSelectedMenu(date)
                    true
                }

                R.id.menuReminderPickDate -> {
                    pickDate()
                    true
                }

                else -> {
                    false
                }
            }
        }
        popupMenu.show()
    }

    private fun pickDate() {
        val calendar = Calendar.getInstance()
        DatePickerDialog(
            view.context,
            R.style.ThemeOverlay_AppCompat_Dialog,
            this,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, day: Int) {
        selectedYear = year
        selectedMonth = month + 1
        selectedDay = day

        val calendar = Calendar.getInstance()
        TimePickerDialog(
            view.context,
            R.style.ThemeOverlay_AppCompat_Dialog,
            this,
            calendar.get(Calendar.HOUR_OF_DAY),
            calendar.get(Calendar.MINUTE),
            true
        ).show()
    }

    override fun onTimeSet(p0: TimePicker?, hour: Int, minute: Int) {
        val localDateTime = LocalDateTime.of(selectedYear, selectedMonth, selectedDay, hour, minute)
        val date = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant())
        onSelectedMenu(date)
    }
}