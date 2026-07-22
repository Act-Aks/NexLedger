package com.actaks.nexledger.feature.goals.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.GoalRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GoalViewModel(
    private val goalRepository: GoalRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GoalState())
    val state: StateFlow<GoalState> = _state.asStateFlow()

    private val _events = Channel<GoalEvent>()
    val events = _events.receiveAsFlow()

    init {
        refresh()
    }

    fun onAction(action: GoalAction) {
        when (action) {
            GoalAction.OnRefresh -> refresh()
            GoalAction.OnAddGoalClick -> navigateToAddGoal()
            is GoalAction.OnGoalClick -> navigateToEditGoal(action.goalId)
            GoalAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun refresh() {
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            goalRepository.getAll().catch { e ->
                _state.update { it.copy(loading = false, error = e.message) }
            }.collect { goals ->
                _state.update { it.copy(goals = goals, loading = false) }
            }
        }
    }

    private fun navigateToAddGoal() {
        viewModelScope.launch {
            _events.send(GoalEvent.NavigateToAddGoal)
        }
    }

    private fun navigateToEditGoal(goalId: Long) {
        viewModelScope.launch {
            _events.send(GoalEvent.NavigateToEditGoal(goalId))
        }
    }
}