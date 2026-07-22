package com.actaks.nexledger.feature.goals.presentation.form

sealed interface GoalFormAction {
    data class OnNameChange(val name: String) : GoalFormAction
    data class OnTargetAmountChange(val amount: String) : GoalFormAction
    data class OnCurrentAmountChange(val amount: String) : GoalFormAction
    data class OnDeadlineSelect(val deadline: Long) : GoalFormAction
    data object OnSave : GoalFormAction
    data object OnErrorDismissed : GoalFormAction
    data object OnBackNavigationClick : GoalFormAction
}