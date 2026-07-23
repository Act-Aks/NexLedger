package com.actaks.nexledger.versioning

import java.io.BufferedReader
import java.io.InputStreamReader

fun findLatestTag(tagPrefix: String = "v"): String? {
    val process = ProcessBuilder(
        "git", "describe", "--tags", "--abbrev=0", "--match", "$tagPrefix*"
    )
        .redirectErrorStream(true)
        .start()

    val out = BufferedReader(InputStreamReader(process.inputStream)).use { it.readText().trim() }
    val exitCode = process.waitFor()
    return if (exitCode == 0 && out.isNotEmpty()) out else null
}

fun parseSemVerFromTag(tag: String, tagPrefix: String = "v"): SemVer? {
    val raw = tag.removePrefix(tagPrefix)
    val parts = raw.split(".")
    if (parts.size != 3) return null

    val major = parts[0].toIntOrNull() ?: return null
    val minor = parts[1].toIntOrNull() ?: return null
    val patch = parts[2].toIntOrNull() ?: return null
    return SemVer(major, minor, patch)
}