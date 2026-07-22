package com.actaks.nexledger.feature.backup.domain

import kotlinx.serialization.Serializable

@Serializable
data class BackupTransaction(
    val amount: Double,
    val type: String,
    val categoryId: Long,
    val accountId: Long,
    val date: Long,
    val note: String,
    val merchant: String
)