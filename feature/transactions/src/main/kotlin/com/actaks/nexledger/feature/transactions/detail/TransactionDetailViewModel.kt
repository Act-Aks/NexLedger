package com.actaks.nexledger.feature.transactions.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.AccountRepository
import com.actaks.nexledger.core.database.repository.CategoryRepository
import com.actaks.nexledger.core.database.repository.TransactionRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionDetailViewModel(
    private val transactionRepo: TransactionRepository,
    private val categoryRepo: CategoryRepository,
    private val accountRepo: AccountRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionDetailState())
    val state: StateFlow<TransactionDetailState> = _state.asStateFlow()

    private val _events = Channel<TransactionDetailEvent>()
    val events = _events.receiveAsFlow()

    private val transactionId: Long = savedStateHandle.get<Long>("transactionId") ?: 0L
    var deleted = false; private set

    init {
        viewModelScope.launch {
            try {
                val transaction = transactionRepo.getById(transactionId)
                val category = transaction?.let { categoryRepo.getById(it.categoryId) }
                val account = transaction?.let { accountRepo.getById(it.accountId) }
                _state.update {
                    it.copy(
                        transaction = transaction,
                        category = category,
                        account = account,
                        loading = false
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(loading = false, error = e.message) }
            }
        }
    }

    fun onAction(action: TransactionDetailAction) {
        when (action) {
            TransactionDetailAction.OnDelete -> _state.update { it.copy(error = "confirm_delete") }
            TransactionDetailAction.OnDeleteConfirm -> deleteConfirm()
            TransactionDetailAction.OnDeleteDismissed -> _state.update { it.copy(error = null) }
            TransactionDetailAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
            TransactionDetailAction.OnBackNavigationClick -> navigateBack()
            is TransactionDetailAction.OnEdit -> navigateToEdit(action.id)
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(TransactionDetailEvent.NavigateBack)
        }
    }

    private fun navigateToEdit(id: Long) {
        viewModelScope.launch {
            _events.send(TransactionDetailEvent.NavigateToEdit(id))
        }
    }

    private fun deleteConfirm() {
        viewModelScope.launch {
            transactionRepo.deleteById(transactionId)
            deleted = true
        }
    }
}
