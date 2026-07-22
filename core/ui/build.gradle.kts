plugins {
    alias(libs.plugins.nexledger.android.library)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.actaks.nexledger.core.ui"
    buildFeatures { compose = true }
}

dependencies {
    implementation(projects.core.model)
    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.graphics)
    implementation(libs.compose.material3)
    implementation(libs.compose.material.icons.extended)
    implementation(libs.lifecycle.runtime.compose)
    implementation(libs.lifecycle.viewmodel.compose)
    
    testImplementation(libs.junit)
    testImplementation(libs.mockk)
    testImplementation(libs.coroutines.test)
    testImplementation(libs.turbine)
}
