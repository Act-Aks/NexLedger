package com.actaks.nexledger.feature.transactions

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.AccountRepository
import com.actaks.nexledger.core.database.repository.CategoryRepository
import com.actaks.nexledger.core.database.repository.TransactionRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionState())
    val state: StateFlow<TransactionState> = _state.asStateFlow()

    private val _events = Channel<TransactionEvent>()
    val events = _events.receiveAsFlow()

    private var observeJob: Job? = null

    init {
        refresh()
    }

    fun onAction(action: TransactionAction) {
        when (action) {
            TransactionAction.OnRefresh -> refresh()
            is TransactionAction.OnTransactionClick -> navigateToTransactionDetail(action.transactionId)
            TransactionAction.OnAddTransactionClick -> navigateToAddTransaction()
            is TransactionAction.OnSearchChange -> {
                _state.update { it.copy(searchQuery = action.query) }
                refresh()
            }

            is TransactionAction.OnFilterTypeSelect -> {
                _state.update { it.copy(filterType = action.type) }
                refresh()
            }

            is TransactionAction.OnFilterCategorySelect -> {
                _state.update { it.copy(filterCategoryId = action.categoryId) }
                refresh()
            }

            is TransactionAction.OnFilterAccountSelect -> {
                _state.update { it.copy(filterAccountId = action.accountId) }
                refresh()
            }

            is TransactionAction.OnSortSelect -> {
                _state.update { it.copy(sortOrder = action.order) }
                refresh()
            }

            TransactionAction.OnToggleFilterSheet ->
                _state.update { it.copy(showFilterSheet = !it.showFilterSheet) }

            TransactionAction.OnClearFilters -> {
                _state.update {
                    it.copy(
                        filterType = null,
                        filterCategoryId = null,
                        filterAccountId = null,
                        searchQuery = ""
                    )
                }
                refresh()
            }

            TransactionAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun refresh() {
        observeJob?.cancel()
        _state.update { it.copy(loading = true, error = null) }
        observeJob = viewModelScope.launch {
            combine(
                transactionRepository.getAll(),
                categoryRepository.getAll(),
                accountRepository.getAll()
            ) { transactions, categories, accounts ->
                _state.update { it.copy(categories = categories, accounts = accounts) }
                val currentState = _state.value
                var filtered = transactions

                // Search
                if (currentState.searchQuery.isNotBlank()) {
                    val queryString = currentState.searchQuery.lowercase()
                    filtered = filtered.filter {
                        it.note.lowercase().contains(queryString) ||
                                it.merchant.lowercase().contains(queryString)
                    }
                }
                // Type filter
                currentState.filterType?.let { type ->
                    filtered = filtered.filter { it.type == type }
                }
                // Category filter
                currentState.filterCategoryId?.let { categoryId ->
                    filtered = filtered.filter { it.categoryId == categoryId }
                }
                // Account filter
                currentState.filterAccountId?.let { accountId ->
                    filtered = filtered.filter { it.accountId == accountId }
                }
                // Sort
                filtered = when (currentState.sortOrder) {
                    SortOrder.DATE_DESC -> filtered.sortedByDescending { it.date }
                    SortOrder.DATE_ASC -> filtered.sortedBy { it.date }
                    SortOrder.AMOUNT_DESC -> filtered.sortedByDescending { it.amount }
                    SortOrder.AMOUNT_ASC -> filtered.sortedBy { it.amount }
                }

                _state.update { it.copy(transactions = filtered, loading = false) }
            }.catch { e ->
                _state.update { it.copy(loading = false, error = e.message) }
            }.collect { }
        }
    }

    private fun navigateToAddTransaction() {
        viewModelScope.launch {
            _events.send(TransactionEvent.NavigateToAddTransaction)
        }
    }

    private fun navigateToTransactionDetail(id: Long) {
        viewModelScope.launch {
            _events.send(TransactionEvent.NavigateToTransactionDetail(id))
        }
    }
}