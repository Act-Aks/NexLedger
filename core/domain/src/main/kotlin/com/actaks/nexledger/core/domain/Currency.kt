package com.actaks.nexledger.core.domain

import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

/** Formats an amount as a locale-aware currency string. */
fun formatCurrency(
    amount: Double,
    currencyCode: String = "INR",
    localeTag: String = "en-IN"
): String {
    val locale = Locale.forLanguageTag(localeTag)

    return try {
        NumberFormat.getCurrencyInstance(locale).apply {
            currency = Currency.getInstance(currencyCode.uppercase(Locale.ROOT))
        }.format(amount)
    } catch (e: IllegalArgumentException) {
        NumberFormat.getCurrencyInstance(locale).format(amount)
    }
}