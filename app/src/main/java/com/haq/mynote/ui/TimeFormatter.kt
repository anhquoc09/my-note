package com.haq.mynote.ui

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatter {

    private const val AN_HOUR_IN_MILLIS = 3600000L

    private const val ONE_MINUTE_IN_SECOND = 60L

    fun format(timestamp: Long): String {
        when {
            (System.currentTimeMillis() - timestamp) < AN_HOUR_IN_MILLIS -> {
                var timestampOffset = System.currentTimeMillis() - timestamp
                timestampOffset = if (timestampOffset >= 0) timestampOffset else 0
                val secondsBefore = timestampOffset / 1000

                return if (secondsBefore < ONE_MINUTE_IN_SECOND) {
                    "$secondsBefore giây"
                } else {
                    (secondsBefore / ONE_MINUTE_IN_SECOND).toString() + " phút"
                }
            }

            isInThisDay(timestamp) -> {
                val format = SimpleDateFormat("hh:mm", Locale.getDefault())
                return format.format(Date(timestamp))
            }

            isInThisWeek(timestamp) -> {
                val format = SimpleDateFormat("hh:mm EEE", Locale.getDefault())
                return format.format(Date(timestamp))
            }
            isInThisYear(timestamp) -> {
                val format = SimpleDateFormat("MMM d", Locale.getDefault())
                return format.format(Date(timestamp))
            }
            else -> {
                val format = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                return format.format(Date(timestamp))
            }
        }
    }

    fun isInThisYear(timestamp: Long): Boolean {
        val currentCalendar = Calendar.getInstance()
        val year = currentCalendar.get(Calendar.YEAR)
        val targetCalendar = Calendar.getInstance()
        val date = Date(timestamp)
        targetCalendar.time = date
        val targetYear = targetCalendar.get(Calendar.YEAR)
        return year == targetYear
    }

    fun isInThisMonth(timestamp: Long): Boolean {
        val currentCalendar = Calendar.getInstance()
        val month = currentCalendar.get(Calendar.MONTH)
        val targetCalendar = Calendar.getInstance()
        val date = Date(timestamp)
        targetCalendar.time = date
        val targetMonth = targetCalendar.get(Calendar.MONTH)
        return month == targetMonth && isInThisYear(timestamp)
    }

    fun isInThisWeek(timestamp: Long): Boolean {
        val currentCalendar = Calendar.getInstance()
        val week = currentCalendar.get(Calendar.WEEK_OF_YEAR)
        val targetCalendar = Calendar.getInstance()
        val date = Date(timestamp)
        targetCalendar.time = date
        val targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR)
        return week == targetWeek && isInThisMonth(timestamp)
    }

    fun isInThisDay(timestamp: Long): Boolean {
        val currentCalendar = Calendar.getInstance()
        val day = currentCalendar.get(Calendar.DATE)
        val targetCalendar = Calendar.getInstance()
        val date = Date(timestamp)
        targetCalendar.time = date
        val targetDay = targetCalendar.get(Calendar.DATE)
        return day == targetDay && isInThisWeek(timestamp)
    }
}