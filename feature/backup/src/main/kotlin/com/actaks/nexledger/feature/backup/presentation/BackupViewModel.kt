package com.actaks.nexledger.feature.backup.presentation

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.actaks.nexledger.feature.backup.data.BackupRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class BackupViewModel(
    private val backupRepository: BackupRepository
) : ViewModel() {

    private val _state = MutableStateFlow(BackupState())
    val state: StateFlow<BackupState> = _state.asStateFlow()

    fun onAction(action: BackupAction) {
        when (action) {
            is BackupAction.OnExportJson -> exportJson()
            is BackupAction.OnExportCsv -> exportCsv()
            is BackupAction.OnImport -> import()
            is BackupAction.OnDismissMessage -> _state.update { it.copy(message = null) }
            is BackupAction.OnErrorDismissed -> _state.update { it.copy(error = null) }
        }
    }

    /** Write exported JSON data to the given URI. */
    fun exportJsonTo(uri: Uri) {
        _state.update { it.copy(isExporting = true) }
        viewModelScope.launch {
            try {
                val result = backupRepository.exportJson(uri)
                _state.update {
                    it.copy(
                        isExporting = false,
                        message = "JSON backup exported (${result} transactions)"
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isExporting = false,
                        error = "Export failed: ${e.message}"
                    )
                }
            }
        }
    }

    /** Write exported CSV data to the given URI. */
    fun exportCsvTo(uri: Uri) {
        _state.update { it.copy(isExporting = true) }
        viewModelScope.launch {
            try {
                val result = backupRepository.exportCsv(uri)
                _state.update {
                    it.copy(isExporting = false, message = "CSV backup exported (${result} rows)")
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isExporting = false,
                        error = "Export failed: ${e.message}"
                    )
                }
            }
        }
    }

    /** Import from a JSON backup URI. */
    fun importFromUri(uri: Uri) {
        _state.update { it.copy(isImporting = true) }
        viewModelScope.launch {
            try {
                val data = backupRepository.importFrom(uri)
                _state.update {
                    it.copy(
                        isImporting = false,
                        message = "Restored ${data.transactions.size} transactions, ${data.accounts.size} accounts, ${data.categories.size} categories"
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        isImporting = false,
                        error = "Import failed: ${e.message}"
                    )
                }
            }
        }
    }

    private fun exportJson() {
        _state.update { it.copy(message = "Choose where to save the JSON file") }
    }

    private fun exportCsv() {
        _state.update { it.copy(message = "Choose where to save the CSV file") }
    }

    private fun import() {
        _state.update { it.copy(message = "Select a .json backup file to restore") }
    }
}
