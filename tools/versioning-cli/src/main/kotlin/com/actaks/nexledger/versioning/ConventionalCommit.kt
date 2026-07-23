package com.actaks.nexledger.versioning

data class ConventionalCommit(
    val type: String,        // feat, fix, chore, etc.
    val scope: String?,
    val description: String,
    val isBreaking: Boolean
)