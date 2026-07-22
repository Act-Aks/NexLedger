package com.actaks.nexledger.core.model


/**
 * A template for transactions that repeat on a schedule.
 *
 * @property id Unique identifier.
 * @property amount Monetary value.
 * @property type [TransactionType] of the generated transactions.
 * @property categoryId FK reference to [Category].
 * @property accountId FK reference to [Account].
 * @property frequency How often the transaction repeats.
 * @property nextExecution Epoch-millis timestamp of the next scheduled occurrence.
 * @property note Optional description.
 */
data class RecurringTransaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType = TransactionType.EXPENSE,
    val categoryId: Long,
    val accountId: Long,
    val frequency: RecurrenceFrequency,
    val nextExecution: Long,
    val note: String = ""
)