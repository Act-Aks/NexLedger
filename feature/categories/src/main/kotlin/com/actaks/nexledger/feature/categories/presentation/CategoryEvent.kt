package com.actaks.nexledger.feature.categories.presentation

sealed interface CategoryEvent {
    data object NavigateToAddCategory : CategoryEvent
    data class NavigateToEditCategory(val categoryId: Long) : CategoryEvent
}