package com.actaks.nexledger.feature.goals.presentation

sealed interface GoalAction {
    data object OnRefresh : GoalAction
    data object OnAddGoalClick : GoalAction
    data class OnGoalClick(val goalId: Long) : GoalAction
    data object OnErrorDismissed : GoalAction
}