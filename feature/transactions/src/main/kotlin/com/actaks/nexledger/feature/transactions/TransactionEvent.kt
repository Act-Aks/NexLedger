package com.actaks.nexledger.feature.transactions

sealed interface TransactionEvent {
    data object NavigateToAddTransaction : TransactionEvent
    data class NavigateToEditTransaction(val id: Long) : TransactionEvent
    data class NavigateToTransactionDetail(val id: Long) : TransactionEvent
}