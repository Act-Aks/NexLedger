package com.actaks.nexledger.feature.backup.domain

import kotlinx.serialization.Serializable

@Serializable
data class BackupAccount(
    val name: String,
    val type: String,
    val balance: Double,
    val currency: String
)