package com.gluco.Utility

import android.annotation.SuppressLint
import android.app.AlertDialog
import java.sql.Timestamp
import java.util.*

@SuppressLint("SimpleDateFormat")
fun String.Companion.timeStampToDateString(date: Long): String {
    val sdf = java.text.SimpleDateFormat("HH:mm MM-dd")
    val out = java.util.Date(date)
    return sdf.format(out)
}

fun String.Companion.empty(): String {
    return ""
}

fun Long.Companion.getDateTimeFromTimestamp(timestamp: Long): Calendar {
    return try {
        val calendar = Calendar.getInstance()
        val timeZone = TimeZone.getDefault()
        calendar.timeInMillis = timestamp
        calendar
    } catch (e: Exception) {
        Calendar.getInstance()
    }
}

fun String.Companion.isEmailValid(email: String): Boolean {
    return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
}