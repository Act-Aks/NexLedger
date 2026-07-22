plugins {
    `kotlin-dsl`
}

group = "com.actaks.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17
    }
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

dependencies {
    compileOnly(libs.android.gradle.plugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradle.plugin)
    compileOnly(libs.ksp.gradle.plugin)
}

gradlePlugin {
    plugins {
        register("androidLibrary") {
            id = "com.actaks.nexledger.android.library"
            implementationClass = "AndroidLibraryConventionPlugin"
        }
        register("feature") {
            id = "com.actaks.nexledger.feature"
            implementationClass = "FeatureConventionPlugin"
        }
    }
}