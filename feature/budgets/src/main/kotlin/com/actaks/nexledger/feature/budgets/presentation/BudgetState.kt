package com.actaks.nexledger.feature.budgets.presentation

import com.actaks.nexledger.core.model.Budget
import com.actaks.nexledger.core.model.Category

data class BudgetState(
    val budgets: List<Budget> = emptyList(),
    val categories: List<Category> = emptyList(),
    val spendingByCategory: Map<Long, Double> = emptyMap(),
    val loading: Boolean = true,
    val error: String? = null
)