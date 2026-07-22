package com.actaks.nexledger.feature.goals.presentation.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.GoalRepository
import com.actaks.nexledger.core.model.Goal
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GoalFormViewModel(
    private val goalRepository: GoalRepository, savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(GoalFormState())
    val state: StateFlow<GoalFormState> = _state.asStateFlow()

    private val _events = Channel<GoalFormEvent>()
    val events = _events.receiveAsFlow()

    private val goalId = savedStateHandle.get<Long>("goalId") ?: 0L

    init {
        if (goalId > 0) {
            _state.update { it.copy(isEditing = true) }
            viewModelScope.launch {
                goalRepository.getById(goalId)?.let { goal ->
                    _state.update {
                        it.copy(
                            name = goal.name,
                            targetAmount = goal.targetAmount.toString(),
                            currentAmount = goal.currentAmount.toString(),
                            deadline = goal.deadline
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: GoalFormAction) {
        when (action) {
            is GoalFormAction.OnNameChange -> _state.update { it.copy(name = action.name) }
            is GoalFormAction.OnTargetAmountChange -> _state.update { it.copy(targetAmount = action.amount) }
            is GoalFormAction.OnCurrentAmountChange -> _state.update { it.copy(currentAmount = action.amount) }
            is GoalFormAction.OnDeadlineSelect -> _state.update { it.copy(deadline = action.deadline) }
            GoalFormAction.OnSave -> save()
            GoalFormAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
            GoalFormAction.OnBackNavigationClick -> navigateBack()
        }
    }

    private fun save() {
        val data = _state.value
        if (data.name.isBlank()) {
            _state.update { it.copy(error = "Name is required") }; return
        }
        val target = data.targetAmount.toDoubleOrNull()
        if (target == null || target <= 0) {
            _state.update { it.copy(error = "Enter a valid target") }; return
        }
        _state.update { it.copy(saving = true) }
        viewModelScope.launch {
            try {
                goalRepository.create(
                    Goal(
                        name = data.name,
                        targetAmount = target,
                        currentAmount = data.currentAmount.toDoubleOrNull() ?: 0.0,
                        deadline = data.deadline
                    )
                )
                _state.update { it.copy(saving = false, saved = true) }
            } catch (e: Exception) {
                _state.update { it.copy(saving = false, error = e.message) }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(GoalFormEvent.NavigateBack)
        }
    }
}
