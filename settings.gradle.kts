@file:Suppress("UnstableApiUsage")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

rootProject.name = "NexLedger"

// App module
include(":app")

// Core modules
include(":core:database")
include(":core:datastore")
include(":core:design-system")
include(":core:domain")
include(":core:model")
include(":core:navigation")
include(":core:ui")

// Feature modules
include(":feature:accounts")
include(":feature:backup")
include(":feature:budgets")
include(":feature:categories")
include(":feature:dashboard")
include(":feature:notifications")
include(":feature:goals")
include(":feature:reports")
include(":feature:search")
include(":feature:security")
include(":feature:settings")
include(":feature:statistics")
include(":feature:transactions")

// tools
include(":tools:versioning-cli")