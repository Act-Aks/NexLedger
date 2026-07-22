package com.actaks.nexledger.feature.budgets.presentation

sealed interface BudgetEvent {
    data object NavigateToAddBudget : BudgetEvent
    data class NavigateToEditBudget(val budgetId: Long) : BudgetEvent
}