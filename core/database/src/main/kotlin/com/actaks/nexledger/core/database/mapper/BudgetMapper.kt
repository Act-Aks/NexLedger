package com.actaks.nexledger.core.database.mapper

import com.actaks.nexledger.core.database.entity.BudgetEntity
import com.actaks.nexledger.core.model.Budget

internal fun BudgetEntity.toDomain() = Budget(
    id = id,
    categoryId = categoryId,
    amount = amount,
    period = period
)

internal fun Budget.toEntity() = BudgetEntity(
    id = id,
    categoryId = categoryId,
    amount = amount,
    period = period
)