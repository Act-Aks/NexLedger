import com.actaks.nexledger.convention.configureKotlinCompile
import com.actaks.nexledger.convention.pluginId
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.JavaVersion
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(pluginId("android.library"))
            apply(pluginId("nexledger.detekt"))
        }

        extensions.getByType<LibraryExtension>().apply {
            compileSdk = 37
            defaultConfig { minSdk = 26 }
            compileOptions {
                sourceCompatibility = JavaVersion.VERSION_17
                targetCompatibility = JavaVersion.VERSION_17
            }
        }
        configureKotlinCompile()
    }
}