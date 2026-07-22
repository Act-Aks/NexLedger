plugins {
    alias(libs.plugins.nexledger.android.library)
    alias(libs.plugins.ksp)
}

android {
    namespace = "com.actaks.nexledger.core.database"
}

dependencies {
    api(libs.room.runtime)
    api(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.koin.android)
    implementation(projects.core.model)
    testImplementation(libs.mockk)
    testImplementation(libs.turbine)
    testImplementation(libs.junit)
    testImplementation(libs.coroutines.test)
}
