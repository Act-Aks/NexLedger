package com.actaks.nexledger.feature.accounts.form

import com.actaks.nexledger.core.model.AccountType

data class AccountFormState(
    val name: String = "",
    val type: AccountType = AccountType.BANK,
    val balance: String = "",
    val currency: String = "INR",
    val isEditing: Boolean = false,
    val saving: Boolean = false,
    val saved: Boolean = false,
    val error: String? = null
)