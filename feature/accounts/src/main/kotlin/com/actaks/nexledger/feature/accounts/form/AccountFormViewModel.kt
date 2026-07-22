package com.actaks.nexledger.feature.accounts.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.AccountRepository
import com.actaks.nexledger.core.model.Account
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountFormViewModel(
    private val accountRepository: AccountRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow(AccountFormState())
    val state: StateFlow<AccountFormState> = _state.asStateFlow()

    private val _events = Channel<AccountFormEvent>()
    val events = _events.receiveAsFlow()

    init {
        val accountId = savedStateHandle.get<Long>("accountId") ?: 0L
        initFormDetails(accountId)
    }

    fun onAction(action: AccountFormAction) {
        when (action) {
            is AccountFormAction.OnBackNavigationClick -> navigateBack()
            is AccountFormAction.OnBalanceChanged -> _state.update { it.copy(balance = action.balance) }
            is AccountFormAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
            is AccountFormAction.OnNameChanged -> _state.update { it.copy(name = action.name) }
            is AccountFormAction.OnSave -> save()
            is AccountFormAction.OnTypeSelected -> _state.update { it.copy(type = action.type) }
        }
    }

    private fun initFormDetails(accountId: Long) {
        if (accountId > 0) {
            _state.update { it.copy(isEditing = true) }
            viewModelScope.launch {
                accountRepository.getById(accountId)?.let { account ->
                    _state.update {
                        it.copy(
                            name = account.name,
                            type = account.type,
                            balance = account.balance.toString(),
                            currency = account.currency
                        )
                    }
                }
            }
        }
    }

    private fun save() {
        val data = _state.value
        if (data.name.isBlank()) {
            _state.update { it.copy(error = "Name is required") }; return
        }
        _state.update { it.copy(saving = true) }
        viewModelScope.launch {
            try {
                val account = Account(
                    name = data.name,
                    type = data.type,
                    balance = data.balance.toDoubleOrNull() ?: 0.0,
                    currency = data.currency
                )
                accountRepository.create(account)
                _state.update { it.copy(saving = false, saved = true) }
            } catch (e: Exception) {
                _state.update { it.copy(saving = false, error = e.message) }
            }
        }
    }

    private fun navigateBack() {
        viewModelScope.launch {
            _events.send(AccountFormEvent.NavigateBack)
        }
    }
}