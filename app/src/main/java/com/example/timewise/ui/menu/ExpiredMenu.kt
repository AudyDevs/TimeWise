package com.example.timewise.ui.menu

import android.app.DatePickerDialog
import android.os.Build
import android.view.ContextThemeWrapper
import android.view.View
import android.widget.DatePicker
import android.widget.PopupMenu
import androidx.annotation.RequiresApi
import com.example.timewise.R
import com.example.timewise.core.Time
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
class ExpiredMenu(private val view: View, private val onSelectedMenu: (Date) -> Unit) :
    DatePickerDialog.OnDateSetListener {

    init {
        val wrapper = ContextThemeWrapper(view.context, R.style.PopupMenu)
        val popupMenu = PopupMenu(wrapper, view)
        popupMenu.inflate(R.menu.menu_expiration)
        popupMenu.setOnMenuItemClickListener { menuItem ->
            val date: Date
            when (menuItem.itemId) {
                R.id.menuExpirationToday -> {
                    date = Time.currentDate()
                    onSelectedMenu(date)
                    true
                }

                R.id.menuExpirationTomorrow -> {
                    date = Time.plusDaysDate(Time.currentDate(), 1)
                    onSelectedMenu(date)
                    true
                }

                R.id.menuExpirationLater -> {
                    date = Time.plusDaysDate(Time.currentDate(), 7)
                    onSelectedMenu(date)
                    true
                }

                R.id.menuExpirationPickDate -> {
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
        val localDate = LocalDate.of(year, month + 1, day)
        val zoneId = ZoneId.systemDefault()
        val date = Date.from(localDate.atStartOfDay(zoneId).toInstant())
        onSelectedMenu(date)
    }
}
