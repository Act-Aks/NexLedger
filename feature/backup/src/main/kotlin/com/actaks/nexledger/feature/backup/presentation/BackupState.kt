package com.actaks.nexledger.feature.backup.presentation

data class BackupState(
    val lastExportTime: Long? = null,
    val isExporting: Boolean = false,
    val isImporting: Boolean = false,
    val message: String? = null,
    val error: String? = null
)