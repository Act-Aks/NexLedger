package com.actaks.nexledger.versioning

import java.io.File

fun gitRootDir(): File {
    val process = ProcessBuilder("git", "rev-parse", "--show-toplevel")
        .redirectErrorStream(true)
        .start()
    val out = process.inputStream.bufferedReader().use { it.readText().trim() }
    process.waitFor()
    return File(out)
}

fun main(args: Array<String>) {
    val gradleRelativePath = args.getOrNull(0) ?: "app/build.gradle.kts"
    val tagPrefix = args.getOrNull(1) ?: "v"

    val root = gitRootDir()
    val gradleFile = File(root, gradleRelativePath)

    val defaultVersion = SemVer(0, 1, 0)

    val lastTag = findLatestTag(tagPrefix)

    val currentVersion: SemVer
    val currentCode: Int

    if (lastTag != null) {
        // Use version from latest tag
        val fromTag = parseSemVerFromTag(lastTag, tagPrefix)
        if (fromTag != null) {
            currentVersion = fromTag
            currentCode = semVerToVersionCode(fromTag)
        } else {
            // If tag parsing fails, fallback to Gradle or default
            val gradleVersion = readGradleVersion(gradleFile)
            if (gradleVersion != null) {
                currentVersion = gradleVersion.versionName
                currentCode = gradleVersion.versionCode
            } else {
                currentVersion = defaultVersion
                currentCode = semVerToVersionCode(defaultVersion)
            }
        }
    } else {
        // No tag yet: prefer version from Gradle
        val gradleVersion = readGradleVersion(gradleFile)
        if (gradleVersion != null) {
            currentVersion = gradleVersion.versionName
            currentCode = gradleVersion.versionCode
        } else {
            // If Gradle parsing fails, fallback
            currentVersion = defaultVersion
            currentCode = semVerToVersionCode(defaultVersion)
        }
    }

    val commits = readGitCommits(lastTag, "HEAD")

    val releaseType = decideReleaseType(commits)
    val newVersion = bumpVersion(currentVersion, releaseType)
    val newCode = semVerToVersionCode(newVersion)

    // Only update Gradle if we actually bump
    if (releaseType != ReleaseType.NONE) {
        updateGradleVersion(gradleFile, newVersion, newCode)
    }

    val tagName = "$tagPrefix$newVersion"

    val json = """
        {
          "oldVersion": "$currentVersion",
          "oldVersionCode": $currentCode,
          "newVersion": "$newVersion",
          "newVersionCode": $newCode,
          "releaseType": "${releaseType.name.lowercase()}",
          "tagName": "$tagName"
        }
    """.trimIndent()

    println(json)
}