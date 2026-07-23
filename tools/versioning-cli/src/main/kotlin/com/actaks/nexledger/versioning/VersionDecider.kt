package com.actaks.nexledger.versioning

fun decideReleaseType(commits: List<ConventionalCommit>): ReleaseType {
    if (commits.isEmpty()) return ReleaseType.NONE

    // Any breaking -> MAJOR
    if (commits.any { it.isBreaking }) {
        return ReleaseType.MAJOR
    }

    // Any feat -> MINOR
    if (commits.any { it.type.equals("feat", ignoreCase = true) }) {
        return ReleaseType.MINOR
    }

    // Patch types
    val patchTypes = setOf(
        "fix", "perf", "refactor", "chore", "docs", "test", "build", "ci"
    )

    if (commits.any { patchTypes.contains(it.type.lowercase()) }) {
        return ReleaseType.PATCH
    }

    return ReleaseType.NONE
}