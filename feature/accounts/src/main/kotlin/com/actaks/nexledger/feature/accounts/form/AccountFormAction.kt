package com.actaks.nexledger.feature.accounts.form

import com.actaks.nexledger.core.model.AccountType

sealed interface AccountFormAction {
    data class OnBalanceChanged(val balance: String) : AccountFormAction
    data class OnNameChanged(val name: String) : AccountFormAction
    data class OnTypeSelected(val type: AccountType) : AccountFormAction
    data object OnBackNavigationClick : AccountFormAction
    data object OnErrorDismissed : AccountFormAction
    data object OnSave : AccountFormAction
}