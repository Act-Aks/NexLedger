package com.actaks.nexledger.feature.budgets.presentation

sealed interface BudgetAction {
    data object OnRefresh : BudgetAction
    data object OnAddBudgetClick : BudgetAction
    data class OnBudgetClick(val budgetId: Long) : BudgetAction
    data object OnErrorDismissed : BudgetAction
}