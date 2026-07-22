import com.actaks.nexledger.convention.pluginId
import dev.detekt.gradle.Detekt
import dev.detekt.gradle.extensions.DetektExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.internal.Actions.with
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.withType

class DetektConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            pluginManager.apply(pluginId("detekt"))

            configure<DetektExtension> {
                parallel.set(true)
                buildUponDefaultConfig.set(true)
                autoCorrect.set(false)
                ignoreFailures.set(false)
                config.from(rootProject.file("config/detekt/detekt.yml"))
                baseline.set(rootProject.file("config/detekt/baseline.xml"))
                basePath.set(rootProject.projectDir)
            }

            tasks.withType<Detekt>().configureEach {
                reports {
                    checkstyle.required.set(true)
                    html.required.set(true)
                    sarif.required.set(true)
                    markdown.required.set(true)
                }
            }
        }
    }
}