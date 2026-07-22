package com.actaks.nexledger.feature.settings

import com.actaks.nexledger.core.domain.ThemeMode

sealed interface SettingsAction {
    data class OnThemeSelect(val mode: ThemeMode) : SettingsAction
    data class OnToggleDynamicColors(val enabled: Boolean) : SettingsAction
    data object OnNavigateToSecurity : SettingsAction
    data object OnNavigateToBackup : SettingsAction
}