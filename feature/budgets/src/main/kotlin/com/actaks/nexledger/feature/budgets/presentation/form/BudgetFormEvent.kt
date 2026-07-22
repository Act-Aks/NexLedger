package com.actaks.nexledger.feature.budgets.presentation.form

sealed interface BudgetFormEvent {
    data object NavigateBack : BudgetFormEvent
}