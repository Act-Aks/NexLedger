import java.io.FileInputStream
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

kotlin {
    jvmToolchain(17)
}

val isReleaseBuild: Boolean = gradle.startParameter.taskNames.any { task ->
    task.contains("Release", ignoreCase = true)
}

// Only enforce keystore.properties for release builds
val keystoreProperties = Properties().apply {
    val propertiesFile = rootProject.file("app/keystore.properties")

    if (!propertiesFile.exists()) {
        if (isReleaseBuild) {
            throw GradleException(
                "Missing keystore.properties at ${propertiesFile.absolutePath}. " + "For release builds, create it locally or generate it in GitHub Actions."
            )
        } else {
            // For debug / non‑release tasks, just return empty props
            return@apply
        }
    }

    load(FileInputStream(propertiesFile))
}

fun requiredKeystoreProp(name: String): String {
    if (!isReleaseBuild) {
        // For debug / non-release tasks, we don't care
        return ""
    }
    return (keystoreProperties.getProperty(name)?.trim()).takeUnless { it.isNullOrEmpty() }
        ?: throw GradleException(
            "Missing required signing property '$name' in keystore.properties. Expected keys: storePassword, keyAlias, keyPassword."
        )
}

android {
    namespace = "com.actaks.nexledger"
    compileSdk { version = release(37) }

    defaultConfig {
        applicationId = "com.actaks.nexledger"
        minSdk = 26
        targetSdk = 37
        versionCode = 1
        versionName = "1.0.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    signingConfigs {
        create("release") {
            if (isReleaseBuild) {
                val storePasswordValue = requiredKeystoreProp("storePassword")
                val keyAliasValue = requiredKeystoreProp("keyAlias")
                val keyPasswordValue = requiredKeystoreProp("keyPassword")

                val storeFileRef = file("release.keystore")
                if (!storeFileRef.exists()) {
                    throw GradleException(
                        "Keystore file not found: ${storeFileRef.absolutePath}. " + "In CI, ensure app/release.keystore is created before assembleRelease."
                    )
                }

                storeFile = storeFileRef
                storePassword = storePasswordValue
                keyAlias = keyAliasValue
                keyPassword = keyPasswordValue
            }
        }
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
            signingConfig = signingConfigs.getByName("release")
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
        debug {
            isMinifyEnabled = false
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-debug"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    // Core Modules
    implementation(projects.core.database)
    implementation(projects.core.datastore)
    implementation(projects.core.designSystem)
    implementation(projects.core.model)
    implementation(projects.core.navigation)
    implementation(projects.core.ui)

    // Feature Modules
    implementation(projects.feature.accounts)
    implementation(projects.feature.backup)
    implementation(projects.feature.budgets)
    implementation(projects.feature.categories)
    implementation(projects.feature.dashboard)
    implementation(projects.feature.goals)
    implementation(projects.feature.notifications)
    implementation(projects.feature.reports)
    implementation(projects.feature.search)
    implementation(projects.feature.security)
    implementation(projects.feature.settings)
    implementation(projects.feature.statistics)
    implementation(projects.feature.transactions)

    // Compose
    implementation(libs.activity.compose)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.compose.ui.tooling)
    debugImplementation(libs.compose.ui.test.manifest)

    // SplashScreen
    implementation(libs.core.splashscreen)

    // Navigation 3
    implementation(libs.navigation3.runtime)
    implementation(libs.navigation3.ui)
    implementation(libs.lifecycle.viewmodel.navigation3)

    // Lifecycle
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    implementation(libs.lifecycle.runtime.ktx)

    // Koin
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.koin.workmanager)

    // WorkManager
    implementation(libs.work.runtime.ktx)

    // Core
    implementation(libs.core.ktx)
    implementation(libs.appcompat)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.koin.test)
    androidTestImplementation(libs.test.runner)
}