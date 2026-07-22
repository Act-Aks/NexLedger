package com.actaks.nexledger.feature.budgets.presentation.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.BudgetRepository
import com.actaks.nexledger.core.database.repository.CategoryRepository
import com.actaks.nexledger.core.model.Budget
import com.actaks.nexledger.core.model.TransactionType
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BudgetFormViewModel(
    private val budgetRepository: BudgetRepository,
    private val categoryRepository: CategoryRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(BudgetFormState())
    val state: StateFlow<BudgetFormState> = _state.asStateFlow()

    private val _events = Channel<BudgetFormEvent>()
    val events = _events.receiveAsFlow()

    private val budgetId = savedStateHandle.get<Long>("budgetId") ?: 0L

    init {
        viewModelScope.launch {
            val categories = categoryRepository.getByType(TransactionType.EXPENSE).first()
            _state.update { it.copy(categories = categories, loading = false) }
            if (budgetId > 0) {
                _state.update { it.copy(isEditing = true) }
                budgetRepository.getById(budgetId)?.let { budget ->
                    _state.update {
                        it.copy(
                            categoryId = budget.categoryId,
                            amount = budget.amount.toString()
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: BudgetFormAction) {
        when (action) {
            is BudgetFormAction.OnBackNavigationClick -> navigateBack()
            is BudgetFormAction.OnCategorySelect -> _state.update { it.copy(categoryId = action.categoryId) }
            is BudgetFormAction.OnAmountChange -> _state.update { it.copy(amount = action.amount) }
            is BudgetFormAction.OnSave -> save()
            is BudgetFormAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun save() {
        val data = _state.value
        val amount = data.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _state.update { it.copy(error = "Enter a valid amount") }
            return
        }
        if (data.categoryId == 0L) {
            _state.update { it.copy(error = "Select a category") }
            return
        }
        _state.update { it.copy(saving = true) }
        viewModelScope.launch {
            try {
                budgetRepository.create(Budget(categoryId = data.categoryId, amount = amount))
                _state.update { it.copy(saving = false, saved = true) }
            } catch (e: Exception) {
                _state.update { it.copy(saving = false, error = e.message) }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(BudgetFormEvent.NavigateBack)
        }
    }
}
