package com.actaks.nexledger.feature.transactions.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.AccountRepository
import com.actaks.nexledger.core.database.repository.CategoryRepository
import com.actaks.nexledger.core.database.repository.TransactionRepository
import com.actaks.nexledger.core.model.Transaction
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class TransactionFormViewModel(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    private val accountRepository: AccountRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(TransactionFormState())
    val state: StateFlow<TransactionFormState> = _state.asStateFlow()

    private val _events = Channel<TransactionFormEvent>()
    val events = _events.receiveAsFlow()

    init {
        val transactionId = savedStateHandle.get<Long>("transactionId") ?: 0L
        val preselectedAccountId = savedStateHandle.get<Long>("accountId") ?: 0L

        viewModelScope.launch {
            val categories = categoryRepository.getAll().first()
            val accounts = accountRepository.getAll().first()
            _state.update { it.copy(categories = categories, accounts = accounts, loading = false) }
            if (preselectedAccountId > 0) _state.update {
                it.copy(accountId = preselectedAccountId)
            }
            if (categories.isNotEmpty() && _state.value.categoryId == 0L) _state.update {
                it.copy(categoryId = categories.first().id)
            }
            if (accounts.isNotEmpty() && _state.value.accountId == 0L) _state.update {
                it.copy(accountId = accounts.first().id)
            }
        }

        if (transactionId > 0) {
            _state.update { it.copy(isEditing = true) }
            viewModelScope.launch {
                transactionRepository.getById(transactionId)?.let { transaction ->
                    _state.update {
                        it.copy(
                            amount = transaction.amount.toString(),
                            type = transaction.type,
                            categoryId = transaction.categoryId,
                            accountId = transaction.accountId,
                            date = transaction.date,
                            note = transaction.note,
                            merchant = transaction.merchant
                        )
                    }
                }
            }
        }
    }

    fun onAction(action: TransactionFormAction) {
        when (action) {
            is TransactionFormAction.OnBackNavigationClick -> navigateBack()
            is TransactionFormAction.OnAmountChange -> _state.update { it.copy(amount = action.amount) }
            is TransactionFormAction.OnTypeSelect -> _state.update { it.copy(type = action.type) }
            is TransactionFormAction.OnCategorySelect -> _state.update { it.copy(categoryId = action.categoryId) }
            is TransactionFormAction.OnAccountSelect -> _state.update { it.copy(accountId = action.accountId) }
            is TransactionFormAction.OnDateSelect -> _state.update { it.copy(date = action.date) }
            is TransactionFormAction.OnNoteChange -> _state.update { it.copy(note = action.note) }
            is TransactionFormAction.OnMerchantChange -> _state.update { it.copy(merchant = action.merchant) }
            TransactionFormAction.OnSave -> save()
            TransactionFormAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun save() {
        val formState = _state.value
        val amount = formState.amount.toDoubleOrNull()
        if (amount == null || amount <= 0) {
            _state.update { it.copy(error = "Enter a valid amount") }; return
        }
        if (formState.categoryId == 0L) {
            _state.update { it.copy(error = "Select a category") }; return
        }
        if (formState.accountId == 0L) {
            _state.update { it.copy(error = "Select an account") }; return
        }
        _state.update { it.copy(saving = true) }
        viewModelScope.launch {
            try {
                val transaction = Transaction(
                    amount = amount,
                    type = formState.type,
                    categoryId = formState.categoryId,
                    accountId = formState.accountId,
                    date = formState.date,
                    note = formState.note,
                    merchant = formState.merchant
                )
                transactionRepository.create(transaction)
                _state.update { it.copy(saving = false, saved = true) }
            } catch (e: Exception) {
                _state.update { it.copy(saving = false, error = e.message) }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(TransactionFormEvent.NavigateBack)
        }
    }
}
