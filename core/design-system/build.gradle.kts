plugins {
    alias(libs.plugins.nexledger.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.actaks.nexledger.core.designsystem"
    buildFeatures { compose = true }
}

dependencies {
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.material3)
    implementation(libs.core.ktx)
    implementation(projects.core.domain)
}