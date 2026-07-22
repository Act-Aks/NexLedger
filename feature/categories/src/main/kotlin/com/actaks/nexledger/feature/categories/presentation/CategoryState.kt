package com.actaks.nexledger.feature.categories.presentation

import com.actaks.nexledger.core.model.Category

data class CategoryState(
    val incomeCategories: List<Category> = emptyList(),
    val expenseCategories: List<Category> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null,
    val selectedTab: CategoryTab = CategoryTab.EXPENSE
)

enum class CategoryTab { INCOME, EXPENSE }