import kotlinx.browser.window
import org.jetbrains.compose.common.foundation.layout.Box
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.renderComposable
import org.jetbrains.webwiz.FileSaverJs
import org.jetbrains.webwiz.JSZip
import org.jetbrains.webwiz.components.Layout
import org.jetbrains.webwiz.components.MainContentLayout
import org.jetbrains.webwiz.content.Header
import org.jetbrains.webwiz.content.Intro
import org.jetbrains.webwiz.content.PageFooter
import org.jetbrains.webwiz.content.WizardSection
import org.jetbrains.webwiz.generator.files.GradleWrapperJar
import org.jetbrains.webwiz.generator.files.Gradlew
import org.jetbrains.webwiz.generator.generate
import org.jetbrains.webwiz.models.ProjectInfo
import org.jetbrains.webwiz.style.AppStylesheet
import org.w3c.files.Blob

@OptIn(ExperimentalComposeWebWidgetsApi::class)
fun main() {
    renderComposable(rootElementId = "root") {
        Box {
            Style(AppStylesheet)

            Layout {
                Header()
                MainContentLayout {
                    Intro()
                    WizardSection { generateProject(it) }
                }
                PageFooter()
            }
        }
    }
}

private fun generateProject(project: ProjectInfo) {
    window.fetch("./binaries/gradle-wrapper")
        .then { response -> response.arrayBuffer() }
        .then { gradleWrapperBlob ->
            val zip = JSZip()
            project.generate().forEach { file ->
                when (file) {
                    is GradleWrapperJar -> zip.file(
                            file.path,
                            gradleWrapperBlob
                    )
                    is Gradlew -> zip.file(
                        file.path,
                        file.content,
                        js("""{unixPermissions:"774"}""") //execution rights
                    )
                    else -> zip.file(
                        file.path,
                        file.content
                    )
                }
            }
            //execution rights require UNIX mode
            zip.generateAsync<Blob>(js("""{type:"blob",platform:"UNIX"}""")).then { blob ->
                FileSaverJs.saveAs(blob, "${project.projectName}.zip")
            }
        }
}
