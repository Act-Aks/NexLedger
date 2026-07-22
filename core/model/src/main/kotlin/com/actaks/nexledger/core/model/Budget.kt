package com.actaks.nexledger.core.model

/**
 * A spending limit for a specific category during a given period.
 *
 * @property id Unique identifier.
 * @property categoryId FK reference to [Category].
 * @property amount Maximum spending allowed in this period.
 * @property period One of [BudgetPeriod].
 */
data class Budget(
    val id: Long = 0,
    val categoryId: Long,
    val amount: Double,
    val period: BudgetPeriod = BudgetPeriod.MONTHLY
)