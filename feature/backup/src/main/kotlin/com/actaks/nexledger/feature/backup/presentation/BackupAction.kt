package com.actaks.nexledger.feature.backup.presentation

import android.net.Uri

sealed interface BackupAction {
    data class OnExportJson(val uri: Uri) : BackupAction
    data class OnExportCsv(val uri: Uri) : BackupAction
    data class OnImport(val uri: Uri) : BackupAction
    data object OnDismissMessage : BackupAction
    data object OnErrorDismissed : BackupAction
}