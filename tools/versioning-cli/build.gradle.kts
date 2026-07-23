plugins {
    alias(libs.plugins.kotlin.jvm)
    application
}

kotlin {
    jvmToolchain(17)
}

dependencies {
    testImplementation(kotlin("test"))
}

application {
    mainClass.set("com.actaks.nexledger.versioning.MainKt")
}