package com.actaks.nexledger.core.database.mapper

import com.actaks.nexledger.core.database.entity.GoalEntity
import com.actaks.nexledger.core.model.Goal

internal fun GoalEntity.toDomain() = Goal(
    id = id,
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    deadline = deadline
)

internal fun Goal.toEntity() = GoalEntity(
    id = id,
    name = name,
    targetAmount = targetAmount,
    currentAmount = currentAmount,
    deadline = deadline
)