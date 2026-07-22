package com.actaks.nexledger.core.database.mapper

import com.actaks.nexledger.core.database.entity.RecurringTransactionEntity
import com.actaks.nexledger.core.model.RecurringTransaction

internal fun RecurringTransactionEntity.toDomain() = RecurringTransaction(
    id = id,
    amount = amount,
    type = type,
    categoryId = categoryId,
    accountId = accountId,
    frequency = frequency,
    nextExecution = nextExecution,
    note = note
)

internal fun RecurringTransaction.toEntity() = RecurringTransactionEntity(
    id = id,
    amount = amount,
    type = type,
    categoryId = categoryId,
    accountId = accountId,
    frequency = frequency,
    nextExecution = nextExecution,
    note = note
)