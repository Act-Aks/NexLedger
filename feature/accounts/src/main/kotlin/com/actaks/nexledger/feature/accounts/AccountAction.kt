package com.actaks.nexledger.feature.accounts

sealed interface AccountAction {
    data object OnRefresh : AccountAction
    data object OnAddAccountClick : AccountAction
    data class OnAccountClick(val accountId: Long) : AccountAction
    data class OnDeleteAccountClick(val accountId: Long) : AccountAction
    data object OnDeleteConfirmed : AccountAction
    data object OnDeleteDismissed : AccountAction
    data object OnErrorDismissed : AccountAction
}