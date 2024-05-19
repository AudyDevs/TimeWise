package com.example.timewise.ui.dialog

import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.example.timewise.R

class DialogDelete(view: View, message: Int, private val onSelectedButton: (Boolean) -> Unit) {

    init {
        val builder = AlertDialog.Builder(view.context)
            .setTitle(R.string.dialogDeleteTitle)
            .setMessage(ContextCompat.getString(view.context, message))
            .setCancelable(false)
            .setPositiveButton(
                R.string.dialogDeleteNo
            ) { _, _ -> onSelectedButton(true) }
            .setNegativeButton(
                R.string.dialogDeleteYes
            ) { _, _ -> onSelectedButton(false) }
            .create()
        builder.show()
        builder.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(ContextCompat.getColor(view.context, R.color.error))
    }
}