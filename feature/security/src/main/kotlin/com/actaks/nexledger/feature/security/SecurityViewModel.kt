package com.actaks.nexledger.feature.security

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.datastore.NexLedgerPreferences
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SecurityViewModel(
    private val preferences: NexLedgerPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(SecurityState())
    val state: StateFlow<SecurityState> = _state.asStateFlow()
    private var savedPin: String = ""

    init {
        viewModelScope.launch {
            preferences.pinEnabled.collect {
                _state.update { s ->
                    s.copy(
                        pinEnabled = it
                    )
                }
            }
        }
        viewModelScope.launch {
            preferences.biometricEnabled.collect {
                _state.update { s ->
                    s.copy(
                        biometricEnabled = it
                    )
                }
            }
        }
    }

    fun onAction(action: SecurityAction) {
        when (action) {
            SecurityAction.OnTogglePin -> {
                if (_state.value.pinEnabled) {
                    viewModelScope.launch {
                        preferences.setPinEnabled(false); preferences.setBiometricEnabled(
                        false
                    )
                    }
                    _state.update {
                        it.copy(
                            pinEnabled = false,
                            biometricEnabled = false,
                            message = "PIN disabled"
                        )
                    }
                } else {
                    _state.update {
                        it.copy(
                            isSettingPin = true,
                            pinInput = "",
                            confirmPinInput = ""
                        )
                    }
                }
            }

            SecurityAction.OnToggleBiometric -> viewModelScope.launch {
                val new = !_state.value.biometricEnabled
                preferences.setBiometricEnabled(new)
                _state.update {
                    it.copy(
                        biometricEnabled = new,
                        message = if (new) "Biometric unlock enabled" else "Biometric unlock disabled"
                    )
                }
            }

            is SecurityAction.OnPinDigitEnter -> {
                val s = _state.value
                if (s.isConfirmingPin) {
                    val newConfirm = s.confirmPinInput + action.digit
                    if (newConfirm.length == 4) {
                        if (newConfirm == savedPin) {
                            viewModelScope.launch { preferences.setPinEnabled(true) }
                            _state.update {
                                it.copy(
                                    isSettingPin = false,
                                    isConfirmingPin = false,
                                    pinInput = "",
                                    confirmPinInput = "",
                                    message = "PIN set successfully"
                                )
                            }
                        } else {
                            _state.update {
                                it.copy(
                                    error = "PINs do not match. Try again.",
                                    isConfirmingPin = false,
                                    pinInput = "",
                                    confirmPinInput = ""
                                )
                            }
                        }
                    } else {
                        _state.update { it.copy(confirmPinInput = newConfirm) }
                    }
                } else {
                    val newPin = s.pinInput + action.digit
                    if (newPin.length == 4) {
                        savedPin = newPin
                        _state.update {
                            it.copy(
                                pinInput = newPin,
                                isConfirmingPin = true,
                                confirmPinInput = ""
                            )
                        }
                    } else {
                        _state.update { it.copy(pinInput = newPin) }
                    }
                }
            }

            SecurityAction.OnPinBackspace -> {
                val s = _state.value
                if (s.isConfirmingPin) _state.update {
                    it.copy(
                        confirmPinInput = s.confirmPinInput.dropLast(
                            1
                        )
                    )
                }
                else _state.update { it.copy(pinInput = s.pinInput.dropLast(1)) }
            }

            SecurityAction.OnPinConfirm -> {}
            SecurityAction.OnDismissMessage -> _state.update { it.copy(message = null) }
            SecurityAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }
}
