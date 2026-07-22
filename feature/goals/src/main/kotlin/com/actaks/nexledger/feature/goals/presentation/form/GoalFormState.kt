package com.actaks.nexledger.feature.goals.presentation.form

data class GoalFormState(
    val name: String = "",
    val targetAmount: String = "",
    val currentAmount: String = "0",
    val deadline: Long = 0,
    val isEditing: Boolean = false,
    val saving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)