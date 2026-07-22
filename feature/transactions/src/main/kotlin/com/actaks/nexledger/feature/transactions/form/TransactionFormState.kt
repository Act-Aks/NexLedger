package com.actaks.nexledger.feature.transactions.form

import com.actaks.nexledger.core.model.Account
import com.actaks.nexledger.core.model.Category
import com.actaks.nexledger.core.model.TransactionType

data class TransactionFormState(
    val amount: String = "",
    val type: TransactionType = TransactionType.EXPENSE,
    val categoryId: Long = 0,
    val accountId: Long = 0,
    val date: Long = System.currentTimeMillis(),
    val note: String = "",
    val merchant: String = "",
    val categories: List<Category> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val isEditing: Boolean = false,
    val saving: Boolean = false,
    val saved: Boolean = false,
    val loading: Boolean = true,
    val error: String? = null
)