package com.actaks.nexledger.feature.transactions

import com.actaks.nexledger.core.model.TransactionType

sealed interface TransactionAction {
    data object OnRefresh : TransactionAction
    data class OnTransactionClick(val transactionId: Long) : TransactionAction
    data object OnAddTransactionClick : TransactionAction
    data class OnSearchChange(val query: String) : TransactionAction
    data class OnFilterTypeSelect(val type: TransactionType?) : TransactionAction
    data class OnFilterCategorySelect(val categoryId: Long?) : TransactionAction
    data class OnFilterAccountSelect(val accountId: Long?) : TransactionAction
    data class OnSortSelect(val order: SortOrder) : TransactionAction
    data object OnToggleFilterSheet : TransactionAction
    data object OnClearFilters : TransactionAction
    data object OnErrorDismissed : TransactionAction
}