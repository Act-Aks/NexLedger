package com.actaks.nexledger.feature.goals.presentation

import com.actaks.nexledger.core.model.Goal

data class GoalState(
    val goals: List<Goal> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null
)