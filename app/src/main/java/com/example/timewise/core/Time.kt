package com.example.timewise.core

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

object Time {
    @SuppressLint("SimpleDateFormat")
    fun CurrentDate(){
        SimpleDateFormat("dd/M/yyyy hh:mm:ss").format(Date())
    }
}