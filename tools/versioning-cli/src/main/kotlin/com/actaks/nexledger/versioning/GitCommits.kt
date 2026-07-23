package com.actaks.nexledger.versioning

import java.io.BufferedReader
import java.io.InputStreamReader

fun readGitCommits(fromRef: String?, toRef: String = "HEAD"): List<ConventionalCommit> {
    val range = if (fromRef != null) "$fromRef..$toRef" else toRef
    val process = ProcessBuilder(
        "git", "log", range, "--pretty=format:%s%n%b%n---END---"
    )
        .redirectErrorStream(true)
        .start()

    val raw = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText() }
    val exitCode = process.waitFor()
    if (exitCode != 0) {
        return emptyList()
    }

    val blocks = raw.split("\n---END---").map { it.trim() }.filter { it.isNotEmpty() }

    return blocks.mapNotNull { block ->
        val lines = block.lines()
        if (lines.isEmpty()) return@mapNotNull null

        val subject = lines.first()
        val body = lines.drop(1).joinToString("\n")

        parseConventionalCommit(subject, body)
    }
}