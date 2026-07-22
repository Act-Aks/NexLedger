package com.actaks.nexledger.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.core.datastore.NexLedgerPreferences
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val preferences: NexLedgerPreferences
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    private val _events = Channel<SettingsEvent>()
    val events = _events.receiveAsFlow()

    init {
        viewModelScope.launch {
            preferences.themeMode.collect {
                _state.update { settingsState -> settingsState.copy(themeMode = it) }
            }
        }
        viewModelScope.launch {
            preferences.useDynamicColors.collect {
                _state.update { settingsState -> settingsState.copy(useDynamicColors = it) }
            }
        }
        viewModelScope.launch {
            preferences.currency.collect {
                _state.update { settingsState -> settingsState.copy(currency = it) }
            }
        }
        viewModelScope.launch {
            preferences.pinEnabled.collect {
                _state.update { settingsState -> settingsState.copy(pinEnabled = it) }
            }
        }
        viewModelScope.launch {
            preferences.biometricEnabled.collect {
                _state.update { settingsState -> settingsState.copy(biometricEnabled = it) }
            }
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.OnThemeSelect -> viewModelScope.launch {
                preferences.setThemeMode(
                    action.mode
                )
            }

            is SettingsAction.OnToggleDynamicColors -> viewModelScope.launch {
                preferences.setUseDynamicColors(
                    action.enabled
                )
            }

            SettingsAction.OnNavigateToSecurity -> {}
            SettingsAction.OnNavigateToBackup -> {}
        }
    }
}
