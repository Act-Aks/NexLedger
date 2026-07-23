package com.actaks.nexledger.versioning

private val headerRegex =
    Regex("""^(?<type>[a-zA-Z]+)(\((?<scope>[^)]+)\))?(?<bang>!)?:\s*(?<desc>.+)$""")

fun parseConventionalHeader(header: String): ConventionalCommit? {
    val match = headerRegex.matchEntire(header.trim()) ?: return null

    val type = match.groups["type"]?.value ?: return null
    val scope = match.groups["scope"]?.value
    val description = match.groups["desc"]?.value?.trim() ?: ""
    val hasBang = match.groups["bang"] != null

    return ConventionalCommit(
        type = type,
        scope = scope,
        description = description,
        isBreaking = hasBang
    )
}

fun detectBreakingChange(body: String): Boolean {
    return body.lines().any { line ->
        line.trim().startsWith("BREAKING CHANGE", ignoreCase = true)
    }
}

fun parseConventionalCommit(subject: String, body: String): ConventionalCommit? {
    val headerCommit = parseConventionalHeader(subject) ?: return null
    val breakingInBody = detectBreakingChange(body)
    return headerCommit.copy(isBreaking = headerCommit.isBreaking || breakingInBody)
}