package com.actaks.nexledger.feature.backup.domain

import kotlinx.serialization.Serializable

@Serializable
data class BackupCategory(
    val name: String,
    val type: String,
    val icon: String,
    val color: String
)