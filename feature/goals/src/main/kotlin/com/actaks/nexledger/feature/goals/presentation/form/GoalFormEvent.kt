package com.actaks.nexledger.feature.goals.presentation.form

sealed interface GoalFormEvent {
    data object NavigateBack : GoalFormEvent
}