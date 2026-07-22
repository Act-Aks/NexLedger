package com.actaks.nexledger.feature.accounts

import com.actaks.nexledger.core.model.Account

data class AccountState(
    val accounts: List<Account> = emptyList(),
    val totalBalance: Double = 0.0,
    val loading: Boolean = true,
    val error: String? = null,
    val showDeleteDialog: Long? = null // account id to confirm deletion
)