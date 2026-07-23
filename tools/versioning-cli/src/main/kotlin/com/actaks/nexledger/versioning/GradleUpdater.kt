package com.actaks.nexledger.versioning

import java.io.File

fun updateGradleVersion(
    gradleFile: File,
    newVersion: SemVer,
    newVersionCode: Int
) {
    val content = gradleFile.readText()

    val updated = content
        .replace(
            Regex("""versionName\s*=\s*"(.*?)""""),
            """versionName = "$newVersion""""
        )
        .replace(
            Regex("""versionCode\s*=\s*\d+"""),
            """versionCode = $newVersionCode"""
        )

    gradleFile.writeText(updated)
}