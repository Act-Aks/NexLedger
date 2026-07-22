package com.actaks.nexledger.feature.search

import com.actaks.nexledger.core.model.Transaction
import com.actaks.nexledger.core.model.TransactionType

data class SearchState(
    val query: String = "",
    val results: List<Transaction> = emptyList(),
    val filterType: TransactionType? = null,
    val filterStartDate: Long? = null,
    val filterEndDate: Long? = null,
    val loading: Boolean = false,
    val error: String? = null
)