package com.actaks.nexledger.feature.transactions.form

sealed interface TransactionFormEvent {
    data object NavigateBack : TransactionFormEvent
}