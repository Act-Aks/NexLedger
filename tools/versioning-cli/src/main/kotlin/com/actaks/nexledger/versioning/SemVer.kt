package com.actaks.nexledger.versioning

data class SemVer(val major: Int, val minor: Int, val patch: Int) {
    override fun toString(): String = "$major.$minor.$patch"
}

fun bumpVersion(current: SemVer, releaseType: ReleaseType): SemVer {
    return when (releaseType) {
        ReleaseType.MAJOR -> SemVer(current.major + 1, 0, 0)
        ReleaseType.MINOR -> SemVer(current.major, current.minor + 1, 0)
        ReleaseType.PATCH -> SemVer(current.major, current.minor, current.patch + 1)
        ReleaseType.NONE -> current
    }
}

fun semVerToVersionCode(semVer: SemVer): Int {
    // Simple deterministic mapping: adjust if you need more headroom
    return semVer.major * 10000 + semVer.minor * 100 + semVer.patch
}