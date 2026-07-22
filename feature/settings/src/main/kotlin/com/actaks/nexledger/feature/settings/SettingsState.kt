package com.actaks.nexledger.feature.settings

import com.actaks.nexledger.core.domain.ThemeMode

data class SettingsState(
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val useDynamicColors: Boolean = true,
    val currency: String = "INR",
    val pinEnabled: Boolean = false,
    val biometricEnabled: Boolean = false,
    val appVersion: String = "1.0.0"
)