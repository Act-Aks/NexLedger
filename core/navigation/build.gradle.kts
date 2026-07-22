plugins {
    alias(libs.plugins.nexledger.android.library)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "com.actaks.nexledger.core.navigation"
    buildFeatures { compose = true }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.koin.android)
    implementation(libs.koin.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.navigation3.runtime)
    implementation(libs.navigation3.ui)
    implementation(libs.lifecycle.viewmodel.navigation3)
    implementation(projects.core.model)

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
}
