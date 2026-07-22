package com.actaks.nexledger.core.domain

import java.util.Calendar

/** Returns epoch millis for the start of the current month (00:00:00.000 day 1). */
fun startOfCurrentMonth(): Long = Calendar.getInstance().apply {
    set(Calendar.DAY_OF_MONTH, 1)
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.timeInMillis

/** Returns epoch millis for the end of the current month (23:59:59.999 last day). */
fun endOfCurrentMonth(): Long = Calendar.getInstance().apply {
    set(Calendar.DAY_OF_MONTH, getActualMaximum(Calendar.DAY_OF_MONTH))
    set(Calendar.HOUR_OF_DAY, 23)
    set(Calendar.MINUTE, 59)
    set(Calendar.SECOND, 59)
    set(Calendar.MILLISECOND, 999)
}.timeInMillis

/** Returns epoch millis for the start of the current year. */
fun startOfCurrentYear(): Long = Calendar.getInstance().apply {
    set(Calendar.DAY_OF_YEAR, 1)
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
}.timeInMillis