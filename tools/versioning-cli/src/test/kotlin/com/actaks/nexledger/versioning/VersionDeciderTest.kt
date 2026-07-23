package com.actaks.nexledger.versioning

import kotlin.test.Test
import kotlin.test.assertEquals

class VersionDeciderTest {

    @Test
    fun `major when any breaking`() {
        val commits = listOf(
            ConventionalCommit("feat", null, "new feature", isBreaking = false),
            ConventionalCommit("fix", null, "bug fix", isBreaking = true)
        )
        assertEquals(ReleaseType.MAJOR, decideReleaseType(commits))
    }

    @Test
    fun `minor when feat but no breaking`() {
        val commits = listOf(
            ConventionalCommit("feat", null, "new feature", isBreaking = false),
            ConventionalCommit("fix", null, "bug fix", isBreaking = false)
        )
        assertEquals(ReleaseType.MINOR, decideReleaseType(commits))
    }

    @Test
    fun `patch when only patch types`() {
        val commits = listOf(
            ConventionalCommit("fix", null, "bug fix", isBreaking = false),
            ConventionalCommit("chore", null, "update deps", isBreaking = false)
        )
        assertEquals(ReleaseType.PATCH, decideReleaseType(commits))
    }

    @Test
    fun `none when no commits`() {
        assertEquals(ReleaseType.NONE, decideReleaseType(emptyList()))
    }
}