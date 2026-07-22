plugins {
    alias(libs.plugins.nexledger.android.library)
}

android {
    namespace = "com.actaks.nexledger.core.model"
}

dependencies {
    api(projects.core.domain)
}
