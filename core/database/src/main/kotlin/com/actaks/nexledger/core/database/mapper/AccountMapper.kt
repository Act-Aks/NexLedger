package com.actaks.nexledger.core.database.mapper

import com.actaks.nexledger.core.database.entity.AccountEntity
import com.actaks.nexledger.core.model.Account

internal fun AccountEntity.toDomain() = Account(
    id = id,
    name = name,
    type = type,
    balance = balance,
    currency = currency,
)

internal fun Account.toEntity() = AccountEntity(
    id = id,
    name = name,
    type = type,
    balance = balance,
    currency = currency
)