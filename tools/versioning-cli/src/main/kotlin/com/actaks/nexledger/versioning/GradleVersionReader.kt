package com.actaks.nexledger.versioning

import java.io.File

data class GradleVersion(
    val versionName: SemVer,
    val versionCode: Int
)

fun readGradleVersion(gradleFile: File): GradleVersion? {
    val content = gradleFile.readText()

    val versionNameMatch = Regex("""versionName\s*=\s*"(.*?)"""")
        .find(content)
    val versionCodeMatch = Regex("""versionCode\s*=\s*(\d+)""")
        .find(content)

    val versionNameStr = versionNameMatch?.groups?.get(1)?.value ?: return null
    val versionCodeStr = versionCodeMatch?.groups?.get(1)?.value ?: return null

    val parts = versionNameStr.split(".")
    if (parts.size != 3) return null

    val major = parts[0].toIntOrNull() ?: return null
    val minor = parts[1].toIntOrNull() ?: return null
    val patch = parts[2].toIntOrNull() ?: return null

    val code = versionCodeStr.toIntOrNull() ?: return null

    return GradleVersion(
        versionName = SemVer(major, minor, patch),
        versionCode = code
    )
}