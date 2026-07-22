package com.actaks.nexledger.feature.categories.presentation

sealed interface CategoryAction {
    data object OnRefresh : CategoryAction
    data class OnTabSelect(val tab: CategoryTab) : CategoryAction
    data object OnAddCategoryClick : CategoryAction
    data class OnCategoryClick(val categoryId: Long) : CategoryAction
    data object OnErrorDismissed : CategoryAction
}