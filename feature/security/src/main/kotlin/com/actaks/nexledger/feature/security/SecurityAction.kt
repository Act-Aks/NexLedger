package com.actaks.nexledger.feature.security

sealed interface SecurityAction {
    data object OnTogglePin : SecurityAction
    data object OnToggleBiometric : SecurityAction
    data class OnPinDigitEnter(val digit: String) : SecurityAction
    data object OnPinBackspace : SecurityAction
    data object OnPinConfirm : SecurityAction
    data object OnDismissMessage : SecurityAction
    data object OnErrorDismissed : SecurityAction
}