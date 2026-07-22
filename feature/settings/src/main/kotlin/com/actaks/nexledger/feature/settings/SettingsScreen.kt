package com.actaks.nexledger.feature.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Backup
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.CurrencyRupee
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actaks.nexledger.core.domain.ThemeMode
import com.actaks.nexledger.core.ui.util.ObserveAsEvents
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SettingsRoot(
    onNavigateToSecurity: () -> Unit,
    onNavigateToBackup: () -> Unit,
    viewModel: SettingsViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEvents(viewModel.events) { event ->
        when (event) {
            SettingsEvent.NavigateToBackup -> onNavigateToBackup()
            SettingsEvent.NavigateToSecurity -> onNavigateToSecurity()
        }
    }

    SettingsScreen(
        state = state,
        onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    state: SettingsState,
    onAction: (SettingsAction) -> Unit
) {
    var showThemeDialog by remember { mutableStateOf(false) }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Theme") },
            text = {
                Column {
                    ThemeMode.entries.forEach { mode ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            RadioButton(
                                selected = state.themeMode == mode,
                                onClick = {
                                    onAction(SettingsAction.OnThemeSelect(mode))
                                    showThemeDialog = false
                                }
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(mode.displayName)
                        }
                    }
                }
            },
            confirmButton = { TextButton(onClick = { showThemeDialog = false }) { Text("OK") } }
        )
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Settings") }) }) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            // Appearance
            ListItem(
                headlineContent = { Text("Theme") },
                supportingContent = { Text(state.themeMode.displayName) },
                leadingContent = { Icon(Icons.Rounded.Palette, null) },
                modifier = Modifier.clickable { showThemeDialog = true })
            ListItem(
                headlineContent = { Text("Dynamic Colors") },
                supportingContent = { Text("Use Material You colors") },
                leadingContent = { Icon(Icons.Rounded.ColorLens, null) },
                trailingContent = {
                    Switch(
                        checked = state.useDynamicColors,
                        onCheckedChange = { onAction(SettingsAction.OnToggleDynamicColors(it)) }
                    )
                }
            )

            HorizontalDivider()

            // Security
            ListItem(
                headlineContent = { Text("Security") },
                supportingContent = { Text("PIN lock, biometrics") },
                leadingContent = { Icon(Icons.Rounded.Lock, null) },
                modifier = Modifier.clickable { onAction(SettingsAction.OnNavigateToSecurity) })

            // Backup
            ListItem(
                headlineContent = { Text("Backup & Restore") },
                supportingContent = { Text("Export and import data") },
                leadingContent = { Icon(Icons.Rounded.Backup, null) },
                modifier = Modifier.clickable { onAction(SettingsAction.OnNavigateToBackup) })

            HorizontalDivider()

            // About
            ListItem(
                headlineContent = { Text("About") },
                supportingContent = { Text("NexLedger v${state.appVersion}") },
                leadingContent = { Icon(Icons.Rounded.Info, null) })
            ListItem(
                headlineContent = { Text("Currency") },
                supportingContent = { Text(state.currency) },
                leadingContent = { Icon(Icons.Rounded.CurrencyRupee, null) })
        }
    }
}

private val ThemeMode.displayName: String
    get() = when (this) {
        ThemeMode.LIGHT -> "Light"; ThemeMode.DARK -> "Dark"; ThemeMode.AMOLED -> "AMOLED"; ThemeMode.SYSTEM -> "System default"
    }