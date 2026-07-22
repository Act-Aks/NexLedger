package com.actaks.nexledger.feature.goals.presentation

sealed interface GoalEvent {
    data object NavigateToAddGoal : GoalEvent
    data class NavigateToEditGoal(val goalId: Long) : GoalEvent
}