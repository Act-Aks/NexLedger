plugins {
    alias(libs.plugins.nexledger.feature)
}

android {
    namespace = "com.actaks.nexledger.feature.backup"
}

dependencies {
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.activity.compose)
}