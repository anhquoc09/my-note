package com.haq.mynote.ui

import java.text.SimpleDateFormat
import java.util.*

object TimeFormatter {

    fun shortFormat(timestamp: Long): String {
        when {
            isInThisDay(timestamp) -> {
                val format = SimpleDateFormat("hh:mm a", Locale.getDefault())
                return format.format(Date(timestamp))
            }
            isInThisWeek(timestamp) -> {
                val format = SimpleDateFormat("eee, hh:mm a", Locale.getDefault())
                return format.format(Date(timestamp))
            }
            isInThisYear(timestamp) -> {
                val format = SimpleDateFormat("MMM d, hh:mm a", Locale.getDefault())
                return format.format(Date(timestamp))
            }
            else -> {
                val format = SimpleDateFormat("MMM d, yyyy", Locale.getDefault())
                return format.format(Date(timestamp))
            }
        }
    }

    fun fullFormat(timestamp: Long): String {
        val format = SimpleDateFormat("MMMM d, yyyy 'at' hh:mm a", Locale.getDefault())
        return format.format(Date(timestamp))
    }

    private fun isInThisYear(timestamp: Long): Boolean {
        val currentCalendar = Calendar.getInstance()
        val year = currentCalendar.get(Calendar.YEAR)
        val targetCalendar = Calendar.getInstance()
        val date = Date(timestamp)
        targetCalendar.time = date
        val targetYear = targetCalendar.get(Calendar.YEAR)
        return year == targetYear
    }

    private fun isInThisMonth(timestamp: Long): Boolean {
        val currentCalendar = Calendar.getInstance()
        val month = currentCalendar.get(Calendar.MONTH)
        val targetCalendar = Calendar.getInstance()
        val date = Date(timestamp)
        targetCalendar.time = date
        val targetMonth = targetCalendar.get(Calendar.MONTH)
        return month == targetMonth && isInThisYear(timestamp)
    }

    private fun isInThisWeek(timestamp: Long): Boolean {
        val currentCalendar = Calendar.getInstance()
        val week = currentCalendar.get(Calendar.WEEK_OF_YEAR)
        val targetCalendar = Calendar.getInstance()
        val date = Date(timestamp)
        targetCalendar.time = date
        val targetWeek = targetCalendar.get(Calendar.WEEK_OF_YEAR)
        return week == targetWeek && isInThisMonth(timestamp)
    }

    private fun isInThisDay(timestamp: Long): Boolean {
        val currentCalendar = Calendar.getInstance()
        val day = currentCalendar.get(Calendar.DATE)
        val targetCalendar = Calendar.getInstance()
        val date = Date(timestamp)
        targetCalendar.time = date
        val targetDay = targetCalendar.get(Calendar.DATE)
        return day == targetDay && isInThisWeek(timestamp)
    }
}