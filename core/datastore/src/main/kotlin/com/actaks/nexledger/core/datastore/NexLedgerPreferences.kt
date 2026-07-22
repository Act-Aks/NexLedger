package com.actaks.nexledger.core.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.actaks.nexledger.core.domain.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "nexledger_prefs")

/**
 * Centralised preferences storage for user settings, theme, and security flags.
 */
class NexLedgerPreferences(
    private val context: Context
) {

    /** Observed theme mode. */
    val themeMode: Flow<ThemeMode> = context.dataStore.data.map { prefs ->
        val name = prefs[THEME_MODE] ?: ThemeMode.SYSTEM.name
        try {
            ThemeMode.valueOf(name)
        } catch (_: IllegalArgumentException) {
            ThemeMode.SYSTEM
        }
    }

    /** Whether dynamic colors are enabled. */
    val useDynamicColors: Flow<Boolean> = context.dataStore.data.map {
        it[USE_DYNAMIC_COLORS] ?: true
    }

    /** Whether PIN lock is enabled. */
    val pinEnabled: Flow<Boolean> = context.dataStore.data.map {
        it[PIN_ENABLED] ?: false
    }

    /** Whether biometric unlock is enabled. */
    val biometricEnabled: Flow<Boolean> = context.dataStore.data.map {
        it[BIOMETRIC_ENABLED] ?: false
    }

    /** Preferred currency code. */
    val currency: Flow<String> = context.dataStore.data.map {
        it[CURRENCY] ?: "INR"
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        context.dataStore.edit { it[THEME_MODE] = mode.name }
    }

    suspend fun setUseDynamicColors(enabled: Boolean) {
        context.dataStore.edit { it[USE_DYNAMIC_COLORS] = enabled }
    }

    suspend fun setPinEnabled(enabled: Boolean) {
        context.dataStore.edit { it[PIN_ENABLED] = enabled }
    }

    suspend fun setBiometricEnabled(enabled: Boolean) {
        context.dataStore.edit { it[BIOMETRIC_ENABLED] = enabled }
    }

    suspend fun setCurrency(code: String) {
        context.dataStore.edit { it[CURRENCY] = code }
    }

    companion object {
        private val THEME_MODE = stringPreferencesKey("theme_mode")
        private val USE_DYNAMIC_COLORS = booleanPreferencesKey("use_dynamic_colors")
        private val PIN_ENABLED = booleanPreferencesKey("pin_enabled")
        private val BIOMETRIC_ENABLED = booleanPreferencesKey("biometric_enabled")
        private val CURRENCY = stringPreferencesKey("currency")
    }
}
