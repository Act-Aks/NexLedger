package com.actaks.nexledger.feature.transactions

import com.actaks.nexledger.core.model.Account
import com.actaks.nexledger.core.model.Category
import com.actaks.nexledger.core.model.Transaction
import com.actaks.nexledger.core.model.TransactionType

data class TransactionState(
    val transactions: List<Transaction> = emptyList(),
    val categories: List<Category> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val loading: Boolean = true,
    val error: String? = null,

    // Filters
    val searchQuery: String = "",
    val filterType: TransactionType? = null,
    val filterCategoryId: Long? = null,
    val filterAccountId: Long? = null,

    // Sort
    val sortOrder: SortOrder = SortOrder.DATE_DESC,

    // UI helpers
    val showFilterSheet: Boolean = false
)

enum class SortOrder { DATE_DESC, DATE_ASC, AMOUNT_DESC, AMOUNT_ASC }