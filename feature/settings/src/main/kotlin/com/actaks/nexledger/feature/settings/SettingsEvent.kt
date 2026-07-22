package com.actaks.nexledger.feature.settings

sealed interface SettingsEvent {
    data object NavigateToSecurity : SettingsEvent
    data object NavigateToBackup : SettingsEvent
}