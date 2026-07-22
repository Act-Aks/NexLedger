package com.actaks.nexledger.convention

import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

internal val Project.libs: VersionCatalog
    get() = extensions.getByType<VersionCatalogsExtension>().named("libs")

internal fun Project.lib(alias: String): Provider<MinimalExternalModuleDependency> =
    libs.findLibrary(alias)
        .orElseThrow { IllegalStateException("Library alias '$alias' not found in libs.versions.toml") }

internal fun Project.pluginId(alias: String): String =
    libs.findPlugin(alias)
        .orElseThrow { IllegalStateException("Plugin alias '$alias' not found in libs.versions.toml") }
        .get()
        .pluginId