package com.actaks.nexledger.feature.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.TransactionRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SearchViewModel(
    private val transactionRepo: TransactionRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SearchState())
    val state: StateFlow<SearchState> = _state.asStateFlow()

    private val _events = Channel<SearchEvent>()
    val events = _events.receiveAsFlow()

    fun onAction(action: SearchAction) {
        when (action) {
            is SearchAction.OnQueryChange -> {
                _state.update { it.copy(query = action.query) }
                search()
            }

            is SearchAction.OnFilterTypeSelect -> {
                _state.update { it.copy(filterType = action.type) }
                search()
            }

            is SearchAction.OnDateRangeSelect -> {
                _state.update {
                    it.copy(
                        filterStartDate = action.start,
                        filterEndDate = action.end
                    )
                }
                search()
            }

            is SearchAction.OnResultClick -> navigateToTransactionDetail(action.transactionId)
            SearchAction.OnClear -> _state.update { SearchState() }
            SearchAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun search() {
        val searchState = _state.value
        if (searchState.query.isBlank() && searchState.filterType == null) {
            _state.update { it.copy(results = emptyList()) }
            return
        }
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            try {
                var results =
                    if (searchState.query.isNotBlank()) transactionRepo.search(searchState.query)
                        .first()
                    else transactionRepo.getAll().first()

                searchState.filterType?.let { type -> results = results.filter { it.type == type } }
                if (searchState.filterStartDate != null && searchState.filterEndDate != null) {
                    results =
                        results.filter { it.date in searchState.filterStartDate..searchState.filterEndDate }
                }
                _state.update {
                    it.copy(
                        results = results.sortedByDescending { r -> r.date },
                        loading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(loading = false, error = e.message) }
            }
        }
    }

    private fun navigateToTransactionDetail(transactionId: Long) {
        viewModelScope.launch {
            _events.send(SearchEvent.NavigateToTransactionDetail(transactionId))
        }
    }
}
