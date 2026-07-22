package com.actaks.nexledger.feature.backup.presentation

import android.net.Uri
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.FileDownload
import androidx.compose.material.icons.rounded.FileUpload
import androidx.compose.material.icons.rounded.TableChart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.actaks.nexledger.core.ui.util.UiText
import com.actaks.nexledger.feature.backup.R
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun BackupRoot(
    viewModel: BackupViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    // File picker for JSON save
    val jsonExportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("application/json")
    ) { uri: Uri? -> uri?.let { viewModel.exportJsonTo(it) } }

    // File picker for CSV save
    val csvExportLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.CreateDocument("text/csv")
    ) { uri: Uri? -> uri?.let { viewModel.exportCsvTo(it) } }

    // File picker for import
    val importLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? -> uri?.let { viewModel.importFromUri(it) } }

    BackupScreen(
        state = state,
        onAction = viewModel::onAction,
        jsonExportLauncher = jsonExportLauncher,
        csvExportLauncher = csvExportLauncher,
        importLauncher = importLauncher
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun BackupScreen(
    state: BackupState,
    onAction: (BackupAction) -> Unit,
    jsonExportLauncher: ManagedActivityResultLauncher<String, Uri?>,
    csvExportLauncher: ManagedActivityResultLauncher<String, Uri?>,
    importLauncher: ManagedActivityResultLauncher<Array<String>, Uri?>,
) {
    // Message dialog
    if (state.message != null) {
        AlertDialog(
            onDismissRequest = { onAction(BackupAction.OnDismissMessage) },
            title = { Text(UiText.StringResourceId(R.string.backup).asString()) },
            text = { Text(state.message) },
            confirmButton = {
                TextButton(onClick = { onAction(BackupAction.OnDismissMessage) }) {
                    Text(UiText.StringResourceId(R.string.ok).asString())
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    UiText.StringResourceId(R.string.backup_and_restore).asString()
                )
            })
        }
    ) { innerPadding ->
        Column(
            Modifier
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = UiText.StringResourceId(R.string.backup_description).asString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            // Error card
            if (state.error != null) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer
                    )
                ) {
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = state.error,
                            color = MaterialTheme.colorScheme.onErrorContainer
                        )
                        TextButton(onClick = { onAction(BackupAction.OnErrorDismissed) }) {
                            Text(UiText.StringResourceId(R.string.dismiss).asString())
                        }
                    }
                }
            }

            // Export Card
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        UiText.StringResourceId(R.string.export).asString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(12.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        OutlinedButton(
                            onClick = { jsonExportLauncher.launch("nexledger_backup.json") },
                            enabled = !state.isExporting
                        ) {
                            Icon(Icons.Rounded.FileDownload, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(UiText.StringResourceId(R.string.export_json).asString())
                        }
                        OutlinedButton(
                            onClick = { csvExportLauncher.launch("nexledger_backup.csv") },
                            enabled = !state.isExporting
                        ) {
                            Icon(Icons.Rounded.TableChart, null, Modifier.size(18.dp))
                            Spacer(Modifier.width(8.dp))
                            Text(UiText.StringResourceId(R.string.export_csv).asString())
                        }
                    }
                }
            }

            // Restore Card
            Card {
                Column(Modifier.padding(16.dp)) {
                    Text(
                        UiText.StringResourceId(R.string.restore).asString(),
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = { importLauncher.launch(arrayOf("application/json", "*/*")) },
                        enabled = !state.isImporting
                    ) {
                        Icon(Icons.Rounded.FileUpload, null, Modifier.size(18.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(
                            if (state.isImporting) UiText.StringResourceId(R.string.importing)
                                .asString()
                            else UiText.StringResourceId(R.string.import_json_backup)
                                .asString()
                        )
                    }
                }
            }

            // Progress bar
            if (state.isExporting || state.isImporting) {
                Spacer(Modifier.height(16.dp))
                LinearProgressIndicator(Modifier.fillMaxWidth())
            }
        }
    }
}
