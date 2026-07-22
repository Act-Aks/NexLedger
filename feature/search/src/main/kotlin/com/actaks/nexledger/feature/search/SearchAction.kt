package com.actaks.nexledger.feature.search

import com.actaks.nexledger.core.model.TransactionType

sealed interface SearchAction {
    data class OnQueryChange(val query: String) : SearchAction
    data class OnFilterTypeSelect(val type: TransactionType?) : SearchAction
    data class OnDateRangeSelect(val start: Long?, val end: Long?) : SearchAction
    data class OnResultClick(val transactionId: Long) : SearchAction
    data object OnClear : SearchAction
    data object OnErrorDismissed : SearchAction
}