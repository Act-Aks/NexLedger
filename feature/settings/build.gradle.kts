plugins {
    alias(libs.plugins.nexledger.feature)
}

android {
    namespace = "com.actaks.nexledger.feature.settings"
}

dependencies {
    implementation(projects.core.datastore)
}