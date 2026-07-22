package com.actaks.nexledger.core.database.mapper

import com.actaks.nexledger.core.database.entity.TransactionEntity
import com.actaks.nexledger.core.model.Transaction

internal fun TransactionEntity.toDomain() = Transaction(
    id = id,
    amount = amount,
    type = type,
    categoryId = categoryId,
    accountId = accountId,
    date = date,
    note = note,
    merchant = merchant,
    paymentMethod = paymentMethod,
    createdAt = createdAt,
)

internal fun Transaction.toEntity() = TransactionEntity(
    id = id,
    amount = amount,
    type = type,
    categoryId = categoryId,
    accountId = accountId,
    date = date,
    note = note,
    merchant = merchant,
    paymentMethod = paymentMethod,
    createdAt = createdAt,
)