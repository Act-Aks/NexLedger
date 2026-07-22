package com.actaks.nexledger.core.model

/**
 * Represents a transaction category with visual identity.
 *
 * @property id Unique identifier.
 * @property name Display name.
 * @property type Whether this category applies to income or expense transactions.
 * @property icon Material icon name for the category.
 * @property color Hex color string (e.g., "#FF5722") for the category chip / icon tint.
 */
data class Category(
    val id: Long = 0,
    val name: String,
    val type: TransactionType,
    val icon: String = "",
    val color: String = ""
)