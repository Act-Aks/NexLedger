package com.actaks.nexledger.core.model

/**
 * Represents a financial account the user tracks.
 *
 * @property id Unique identifier.
 * @property name Display name (e.g., "Chase Checking").
 * @property type One of [AccountType].
 * @property balance Current balance in the account's currency.
 * @property currency ISO 4217 currency code (e.g., "USD", "INR").
 */
data class Account(
    val id: Long = 0,
    val name: String,
    val type: AccountType,
    val balance: Double = 0.0,
    val currency: String = "INR"
)
