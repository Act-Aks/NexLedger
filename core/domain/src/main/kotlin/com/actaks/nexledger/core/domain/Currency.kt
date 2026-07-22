package com.actaks.nexledger.core.domain

import java.text.NumberFormat
import java.util.Locale

/** Formats a Double as a locale-aware currency string. */
fun formatCurrency(amount: Double, currencyCode: String = "INR"): String {
    val format = NumberFormat.getCurrencyInstance(Locale("en", "IN"))
    return format.format(amount)
}