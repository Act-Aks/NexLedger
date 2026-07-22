package com.actaks.nexledger.core.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.actaks.nexledger.core.model.RecurrenceFrequency
import com.actaks.nexledger.core.model.TransactionType

@Entity(tableName = "recurring_transactions")
data class RecurringTransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val amount: Double,
    val type: TransactionType = TransactionType.EXPENSE,
    val categoryId: Long,
    val accountId: Long,
    val frequency: RecurrenceFrequency,
    val nextExecution: Long,
    val note: String = ""
)