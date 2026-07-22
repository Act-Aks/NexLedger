package com.actaks.nexledger.core.model

/**
 * Represents a single financial transaction (income or expense).
 *
 * @property id Unique identifier assigned by Room on insert.
 * @property amount Monetary value of the transaction (always stored positive).
 * @property type Either [TransactionType.INCOME] or [TransactionType.EXPENSE].
 * @property categoryId FK reference to [Category].
 * @property accountId FK reference to [Account].
 * @property date Epoch-millis timestamp of when the transaction occurred.
 * @property note Optional user-provided description.
 * @property merchant Optional payee / merchant name.
 * @property paymentMethod Optional payment instrument (cash, card, etc.).
 * @property createdAt Epoch-millis timestamp of when this record was created.
 */
data class Transaction(
    val id: Long = 0,
    val amount: Double,
    val type: TransactionType,
    val categoryId: Long,
    val accountId: Long,
    val date: Long,
    val note: String = "",
    val merchant: String = "",
    val paymentMethod: String = "",
    val createdAt: Long = System.currentTimeMillis()
)