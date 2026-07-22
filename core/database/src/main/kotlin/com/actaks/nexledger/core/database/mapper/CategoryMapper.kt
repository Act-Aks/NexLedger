package com.actaks.nexledger.core.database.mapper

import com.actaks.nexledger.core.database.entity.CategoryEntity
import com.actaks.nexledger.core.model.Category

internal fun CategoryEntity.toDomain() = Category(
    id = id,
    name = name,
    type = type,
    icon = icon,
    color = color,
)

internal fun Category.toEntity() = CategoryEntity(
    id = id,
    name = name,
    type = type,
    icon = icon,
    color = color,
)