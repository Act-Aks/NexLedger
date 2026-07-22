package com.actaks.nexledger.feature.backup.domain

import kotlinx.serialization.Serializable

@Serializable
data class BackupData(
    val version: Int = 1,
    val transactions: List<BackupTransaction> = emptyList(),
    val accounts: List<BackupAccount> = emptyList(),
    val categories: List<BackupCategory> = emptyList()
)