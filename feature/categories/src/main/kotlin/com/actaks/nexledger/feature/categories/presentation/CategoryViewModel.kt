package com.actaks.nexledger.feature.categories.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.CategoryRepository
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

class CategoryViewModel constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryState())
    val state: StateFlow<CategoryState> = _state.asStateFlow()

    private val _events = Channel<CategoryEvent>()
    val events = _events.receiveAsFlow()

    init {
        refresh()
    }

    fun onAction(action: CategoryAction) {
        when (action) {
            is CategoryAction.OnRefresh -> refresh()
            is CategoryAction.OnTabSelect -> _state.update { it.copy(selectedTab = action.tab) }
            is CategoryAction.OnAddCategoryClick -> navigateToAddCategory()
            is CategoryAction.OnCategoryClick -> navigateToEditCategory(action.categoryId)
            is CategoryAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun refresh() {
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            combine(
                categoryRepository.getByType(TransactionType.INCOME),
                categoryRepository.getByType(TransactionType.EXPENSE)
            ) { income, expense ->
                _state.update {
                    it.copy(
                        incomeCategories = income,
                        expenseCategories = expense,
                        loading = false
                    )
                }
            }.catch { e ->
                _state.update { it.copy(loading = false, error = e.message) }
            }.collect { }
        }
    }

    private fun navigateToAddCategory() {
        viewModelScope.launch {
            _events.send(CategoryEvent.NavigateToAddCategory)
        }
    }

    private fun navigateToEditCategory(categoryId: Long) {
        viewModelScope.launch {
            _events.send(CategoryEvent.NavigateToEditCategory(categoryId))
        }
    }
}