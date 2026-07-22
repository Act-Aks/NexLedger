package com.actaks.nexledger.feature.budgets.presentation.form

import com.actaks.nexledger.core.model.Category

data class BudgetFormState(
    val categoryId: Long = 0,
    val amount: String = "",
    val categories: List<Category> = emptyList(),
    val isEditing: Boolean = false,
    val saving: Boolean = false,
    val saved: Boolean = false,
    val loading: Boolean = true,
    val error: String? = null
)