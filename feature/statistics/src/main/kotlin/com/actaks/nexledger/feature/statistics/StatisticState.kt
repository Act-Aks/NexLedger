package com.actaks.nexledger.feature.statistics

import com.actaks.nexledger.core.model.Category

data class StatisticState(
    val totalIncome: Double = 0.0,
    val totalExpenses: Double = 0.0,
    val categoryBreakdown: List<CategorySpending> = emptyList(),
    val categories: List<Category> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null
)

data class CategorySpending(
    val categoryId: Long,
    val categoryName: String,
    val amount: Double,
    val percentage: Float
)