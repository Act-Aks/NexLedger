package com.actaks.nexledger.feature.security

data class SecurityState(
    val pinEnabled: Boolean = false,
    val biometricEnabled: Boolean = false,
    val isBiometricAvailable: Boolean = false,
    val pinInput: String = "",
    val confirmPinInput: String = "",
    val isSettingPin: Boolean = false,
    val isConfirmingPin: Boolean = false,
    val message: String? = null,
    val error: String? = null
)