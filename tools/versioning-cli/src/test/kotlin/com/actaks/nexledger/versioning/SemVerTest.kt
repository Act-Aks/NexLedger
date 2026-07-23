package com.actaks.nexledger.versioning

import kotlin.test.Test
import kotlin.test.assertEquals

class SemVerTest {

    @Test
    fun `bump major`() {
        val current = SemVer(1, 2, 3)
        val next = bumpVersion(current, ReleaseType.MAJOR)
        assertEquals(SemVer(2, 0, 0), next)
    }

    @Test
    fun `bump minor`() {
        val current = SemVer(1, 2, 3)
        val next = bumpVersion(current, ReleaseType.MINOR)
        assertEquals(SemVer(1, 3, 0), next)
    }

    @Test
    fun `bump patch`() {
        val current = SemVer(1, 2, 3)
        val next = bumpVersion(current, ReleaseType.PATCH)
        assertEquals(SemVer(1, 2, 4), next)
    }

    @Test
    fun `versionCode mapping`() {
        val v = SemVer(1, 2, 3)
        val code = semVerToVersionCode(v)
        assertEquals(10203, code)
    }
}