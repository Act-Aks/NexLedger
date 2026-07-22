package com.actaks.nexledger.feature.transactions.detail

sealed interface TransactionDetailEvent {
    data object NavigateBack : TransactionDetailEvent
    data class NavigateToEdit(val id: Long) : TransactionDetailEvent
    data object Deleted : TransactionDetailEvent
}