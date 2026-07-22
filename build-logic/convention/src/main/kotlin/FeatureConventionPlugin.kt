import com.actaks.nexledger.convention.lib
import com.actaks.nexledger.convention.pluginId
import com.android.build.api.dsl.LibraryExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.getByType

class FeatureConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply(pluginId("nexledger.android.library"))
            apply(pluginId("kotlin.compose"))

            extensions.getByType<LibraryExtension>().apply {
                buildFeatures { compose = true }
            }

            dependencies {
                "implementation"(project(":core:model"))
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:design-system"))
                "implementation"(project(":core:database"))

                "implementation"(platform(lib("compose.bom")))
                "implementation"(lib("compose.ui"))
                "implementation"(lib("compose.ui.graphics"))
                "implementation"(lib("compose.material3"))
                "implementation"(lib("compose.material.icons.extended"))
                "implementation"(lib("lifecycle.runtime.compose"))
                "implementation"(lib("lifecycle.viewmodel.compose"))
                "implementation"(lib("koin.android"))
                "implementation"(lib("koin.compose"))
                "implementation"(lib("kotlinx.coroutines.android"))

                "debugImplementation"(lib("compose.ui.tooling"))
                "debugImplementation"(lib("compose.ui.tooling.preview"))

                "testImplementation"(lib("junit"))
                "testImplementation"(lib("mockk"))
                "testImplementation"(lib("coroutines.test"))
                "testImplementation"(lib("turbine"))
                "testImplementation"(lib("koin.test"))
            }
        }
    }
}