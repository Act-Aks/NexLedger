package com.actaks.nexledger.feature.backup.data

import android.net.Uri
import com.actaks.nexledger.feature.backup.domain.BackupData

sealed interface BackupRepository {
    suspend fun exportJson(uri: Uri): Int
    suspend fun exportCsv(uri: Uri): Int
    suspend fun importFrom(uri: Uri): BackupData
}