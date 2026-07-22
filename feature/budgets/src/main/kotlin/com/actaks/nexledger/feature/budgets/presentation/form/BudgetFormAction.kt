package com.actaks.nexledger.feature.budgets.presentation.form

sealed interface BudgetFormAction {
    data class OnCategorySelect(val categoryId: Long) : BudgetFormAction
    data class OnAmountChange(val amount: String) : BudgetFormAction
    data object OnSave : BudgetFormAction
    data object OnErrorDismissed : BudgetFormAction
    data object OnBackNavigationClick : BudgetFormAction
}