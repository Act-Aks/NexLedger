package com.actaks.nexledger.feature.accounts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.database.repository.AccountRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AccountViewModel(
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _state = MutableStateFlow(AccountState())
    val state: StateFlow<AccountState> = _state.asStateFlow()

    private val _events = Channel<AccountEvent>()
    val events = _events.receiveAsFlow()

    private var pendingDeleteId: Long? = null

    init {
        refresh()
    }

    fun onAction(action: AccountAction) {
        when (action) {
            is AccountAction.OnRefresh -> refresh()
            is AccountAction.OnAddAccountClick -> navigateToAddAccount()
            is AccountAction.OnAccountClick -> navigateToEditAccount(action.accountId)
            is AccountAction.OnDeleteAccountClick -> openDeleteAccountDialog(action.accountId)
            is AccountAction.OnDeleteConfirmed -> deleteAccount()
            is AccountAction.OnDeleteDismissed -> closeDeleteAccountDialog()
            is AccountAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    private fun refresh() {
        _state.update { it.copy(loading = true, error = null) }
        viewModelScope.launch {
            accountRepository.getAll().catch { e ->
                _state.update { it.copy(loading = false, error = e.message) }
            }.collect { accounts ->
                val total = accountRepository.getTotalBalance()
                _state.update {
                    it.copy(
                        accounts = accounts,
                        totalBalance = total,
                        loading = false
                    )
                }
            }
        }
    }

    private fun openDeleteAccountDialog(accountId: Long) {
        pendingDeleteId = accountId
        _state.update { it.copy(showDeleteDialog = accountId) }
    }

    private fun closeDeleteAccountDialog() {
        pendingDeleteId = null
        _state.update { it.copy(showDeleteDialog = null) }
    }

    private fun deleteAccount() {
        val id = pendingDeleteId ?: return
        pendingDeleteId = null
        _state.update { it.copy(showDeleteDialog = null) }
        viewModelScope.launch {
            accountRepository.deleteById(id)
        }
    }

    private fun navigateToAddAccount() {
        viewModelScope.launch {
            _events.send(AccountEvent.NavigateToAddAccount)
        }
    }

    private fun navigateToEditAccount(accountId: Long) {
        viewModelScope.launch {
            _events.send(AccountEvent.NavigateToEditAccount(accountId))
        }
    }
}