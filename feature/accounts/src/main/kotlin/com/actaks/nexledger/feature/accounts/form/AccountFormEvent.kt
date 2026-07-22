package com.actaks.nexledger.feature.accounts.form

sealed interface AccountFormEvent {
    data object NavigateBack : AccountFormEvent
}