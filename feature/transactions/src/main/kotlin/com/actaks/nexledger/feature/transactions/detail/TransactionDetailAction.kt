package com.actaks.nexledger.feature.transactions.detail

sealed interface TransactionDetailAction {
    data class OnEdit(val id: Long) : TransactionDetailAction
    data object OnDelete : TransactionDetailAction
    data object OnDeleteConfirm : TransactionDetailAction
    data object OnDeleteDismissed : TransactionDetailAction
    data object OnErrorDismissed : TransactionDetailAction
    data object OnBackNavigationClick : TransactionDetailAction
}