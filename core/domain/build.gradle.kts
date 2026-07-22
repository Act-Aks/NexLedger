plugins {
    alias(libs.plugins.nexledger.android.library)
}

android {
    namespace = "com.actaka.nexledger.core.domain"
}

dependencies {
    api(libs.kotlin.stdlib)
    api(libs.kotlinx.coroutines.core)
    testImplementation(libs.junit)
}