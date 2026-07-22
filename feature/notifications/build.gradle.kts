plugins {
    alias(libs.plugins.nexledger.feature)
}

android {
    namespace = "com.actaks.nexledger.feature.notifications"
}

dependencies {
    implementation(libs.work.runtime.ktx)
    implementation(libs.koin.workmanager)
}