package com.actaks.nexledger.feature.budgets.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.BudgetRepository
import com.actaks.nexledger.core.database.repository.CategoryRepository
import com.actaks.nexledger.core.database.repository.TransactionRepository
import com.actaks.nexledger.core.model.TransactionType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Calendar

class BudgetViewModel(
    private val budgetRepository: BudgetRepository,
    private val categoryRepository: CategoryRepository,
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetState())
    val state: StateFlow<BudgetState> = _state.asStateFlow()

    private val _events = Channel<BudgetEvent>()
    val events = _events.receiveAsFlow()

    init {
        refresh()
    }

    fun onAction(action: BudgetAction) {
        when (action) {
            is BudgetAction.OnRefresh -> refresh()
            is BudgetAction.OnAddBudgetClick -> navigateToAddBudget()
            is BudgetAction.OnBudgetClick -> navigateToAddBudget(action.budgetId)
            is BudgetAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun refresh() {
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            combine(
                budgetRepository.getAll(),
                categoryRepository.getAll()
            ) { budgets, categories ->
                _state.update { it.copy(budgets = budgets, categories = categories) }
                computeSpending()
            }.catch { e ->
                _state.update { it.copy(loading = false, error = e.message) }
            }.collect {}
        }
    }

    private suspend fun computeSpending() {
        val now = Calendar.getInstance()
        val start = now.clone() as Calendar
        start.set(Calendar.DAY_OF_MONTH, 1)
        start.set(Calendar.HOUR_OF_DAY, 0)
        start.set(Calendar.MINUTE, 0)
        start.set(Calendar.SECOND, 0)
        start.set(Calendar.MILLISECOND, 0)

        val end = now.clone() as Calendar
        end.set(Calendar.DAY_OF_MONTH, end.getActualMaximum(Calendar.DAY_OF_MONTH))
        end.set(Calendar.HOUR_OF_DAY, 23); end.set(Calendar.MINUTE, 59)
        end.set(Calendar.SECOND, 59)

        val totalExpense = transactionRepository.sumByTypeAndDateRange(
            TransactionType.EXPENSE, start.timeInMillis, end.timeInMillis
        )
        val spendingMap = mutableMapOf<Long, Double>()
        for (budget in _state.value.budgets) {
            spendingMap[budget.categoryId] = totalExpense
        }
        _state.update { it.copy(spendingByCategory = spendingMap, loading = false) }
    }

    private fun navigateToAddBudget() {
        viewModelScope.launch {
            _events.send(BudgetEvent.NavigateToAddBudget)
        }
    }

    private fun navigateToAddBudget(budgetId: Long) {
        viewModelScope.launch {
            _events.send(BudgetEvent.NavigateToEditBudget(budgetId))
        }
    }
}
