package com.actaks.nexledger.feature.transactions.detail

import com.actaks.nexledger.core.model.Account
import com.actaks.nexledger.core.model.Category
import com.actaks.nexledger.core.model.Transaction

data class TransactionDetailState(
    val transaction: Transaction? = null,
    val category: Category? = null,
    val account: Account? = null,
    val loading: Boolean = true,
    val error: String? = null
)