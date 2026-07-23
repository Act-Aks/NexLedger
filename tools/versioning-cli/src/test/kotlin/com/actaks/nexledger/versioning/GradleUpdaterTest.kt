package com.actaks.nexledger.versioning

import java.io.File
import kotlin.test.Test
import kotlin.test.assertTrue

class GradleUpdaterTest {

    @Test
    fun `updates versionName and versionCode`() {
        val tmpFile = File.createTempFile("build", ".gradle.kts")
        tmpFile.writeText(
            """
            android {
                defaultConfig {
                    applicationId = "com.actaks.nexledger"
                    minSdk = 26
                    targetSdk = 37
                    versionCode = 1
                    versionName = "1.0.0"
                }
            }
            """.trimIndent()
        )

        val newVersion = SemVer(1, 2, 3)
        val newCode = semVerToVersionCode(newVersion)

        updateGradleVersion(tmpFile, newVersion, newCode)

        val content = tmpFile.readText()
        assertTrue(content.contains("""versionName = "1.2.3""""))
        assertTrue(content.contains("""versionCode = $newCode"""))
    }
}