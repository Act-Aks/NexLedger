package com.actaks.nexledger.core.model

/**
 * A savings goal the user is working towards.
 *
 * @property id Unique identifier.
 * @property name Display name (e.g., "Emergency Fund").
 * @property targetAmount Total amount needed.
 * @property currentAmount Amount saved so far.
 * @property deadline Epoch-millis timestamp of the target date, or 0 if open-ended.
 */
data class Goal(
    val id: Long = 0,
    val name: String,
    val targetAmount: Double,
    val currentAmount: Double = 0.0,
    val deadline: Long = 0
)