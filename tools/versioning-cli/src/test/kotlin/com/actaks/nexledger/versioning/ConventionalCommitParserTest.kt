package com.actaks.nexledger.versioning

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ConventionalCommitParserTest {

    @Test
    fun `parses simple feat commit`() {
        val header = "feat: add new dashboard"
        val commit = parseConventionalHeader(header)
        assertNotNull(commit)
        assertEquals("feat", commit.type)
        assertNull(commit.scope)
        assertEquals("add new dashboard", commit.description)
        assertEquals(false, commit.isBreaking)
    }

    @Test
    fun `parses feat with scope and bang`() {
        val header = "feat(ui)!: change layout"
        val commit = parseConventionalHeader(header)
        assertNotNull(commit)
        assertEquals("feat", commit.type)
        assertEquals("ui", commit.scope)
        assertTrue(commit.isBreaking)
    }

    @Test
    fun `detects BREAKING CHANGE in body`() {
        val subject = "fix: adjust calculation"
        val body = """
            Some explanation

            BREAKING CHANGE: the budget calculation is now per day instead of per month
        """.trimIndent()

        val commit = parseConventionalCommit(subject, body)
        assertNotNull(commit)
        assertEquals("fix", commit.type)
        assertTrue(commit.isBreaking)
    }

    @Test
    fun `non conventional header returns null`() {
        val header = "this is not conventional"
        val commit = parseConventionalHeader(header)
        assertNull(commit)
    }
}