plugins {
    alias(libs.plugins.nexledger.android.library)
}

android {
    namespace = "com.actaks.nexledger.core.datastore"
}

dependencies {
    implementation(libs.datastore.preferences)
    implementation(libs.koin.android)
    implementation(projects.core.domain)
}