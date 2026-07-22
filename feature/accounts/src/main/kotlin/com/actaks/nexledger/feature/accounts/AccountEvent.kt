package com.actaks.nexledger.feature.accounts

sealed interface AccountEvent {
    data object NavigateToAddAccount : AccountEvent
    data class NavigateToEditAccount(val accountId: Long) : AccountEvent
}