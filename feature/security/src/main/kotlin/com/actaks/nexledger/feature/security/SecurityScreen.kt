package com.actaks.nexledger.feature.security

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CheckCircleOutline
import androidx.compose.material.icons.rounded.Circle
import androidx.compose.material.icons.rounded.Fingerprint
import androidx.compose.material.icons.rounded.Pin
import androidx.compose.material.icons.rounded.Shield
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun SecurityRoot(
    viewModel: SecurityViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    SecurityScreen(
        state = state, onAction = viewModel::onAction
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SecurityScreen(
    state: SecurityState,
    onAction: (SecurityAction) -> Unit
) {
    if (state.message != null) {
        AlertDialog(
            onDismissRequest = { onAction(SecurityAction.OnDismissMessage) },
            title = { Text("Security") },
            text = { Text(state.message) },
            confirmButton = {
                TextButton(onClick = { onAction(SecurityAction.OnDismissMessage) }) {
                    Text(
                        "OK"
                    )
                }
            })
    }
    if (state.error != null) {
        AlertDialog(
            onDismissRequest = { onAction(SecurityAction.OnErrorDismissed) },
            title = { Text("Error") },
            text = { Text(state.error) },
            confirmButton = {
                TextButton(onClick = { onAction(SecurityAction.OnErrorDismissed) }) {
                    Text(
                        "OK"
                    )
                }
            })
    }

    Scaffold(topBar = { TopAppBar(title = { Text("Security") }) }) { innerPadding ->
        Column(Modifier.padding(innerPadding)) {
            if (state.isSettingPin) {
                PinSetupSheet(state, onAction)
            } else {
                ListItem(
                    headlineContent = { Text("PIN Lock") },
                    supportingContent = { Text(if (state.pinEnabled) "Enabled" else "Disabled") },
                    leadingContent = { Icon(Icons.Rounded.Pin, null) },
                    trailingContent = {
                        Switch(
                            checked = state.pinEnabled,
                            onCheckedChange = { onAction(SecurityAction.OnTogglePin) })
                    }
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text("Biometric Unlock") },
                    supportingContent = { Text("Use fingerprint to unlock") },
                    leadingContent = { Icon(Icons.Rounded.Fingerprint, null) },
                    trailingContent = {
                        Switch(
                            checked = state.biometricEnabled,
                            onCheckedChange = { onAction(SecurityAction.OnToggleBiometric) }
                        )
                    },
//                    enabled = state.pinEnabled
                )
                HorizontalDivider()
                ListItem(
                    headlineContent = { Text("Data Privacy") },
                    supportingContent = { Text("All data is stored locally on your device") },
                    leadingContent = { Icon(Icons.Rounded.Shield, null) }
                )
            }
        }
    }
}

@Composable
private fun PinSetupSheet(state: SecurityState, onAction: (SecurityAction) -> Unit) {
    val display = if (state.isConfirmingPin) state.confirmPinInput else state.pinInput
    Column(
        Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            if (state.isConfirmingPin) "Confirm your PIN" else "Set a 4-digit PIN",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(Modifier.height(24.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            repeat(4) { i ->
                Box(Modifier.size(24.dp)) {
                    Icon(
                        if (i < display.length) Icons.Rounded.Circle else Icons.Rounded.CheckCircleOutline,
                        null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
        }
        Spacer(Modifier.height(32.dp))
        PinNumpad(onAction)
    }
}

@Composable
private fun PinNumpad(onAction: (SecurityAction) -> Unit) {
    val digits = listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "", "0", "⌫")
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        digits.chunked(3).forEach { row ->
            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                row.forEach { digit ->
                    if (digit.isEmpty()) Spacer(Modifier.size(64.dp))
                    else TextButton(
                        onClick = {
                            if (digit == "⌫") onAction(SecurityAction.OnPinBackspace)
                            else onAction(SecurityAction.OnPinDigitEnter(digit))
                        },
                        modifier = Modifier.size(64.dp)
                    ) {
                        Text(digit, style = MaterialTheme.typography.headlineMedium)
                    }
                }
            }
        }
    }
}