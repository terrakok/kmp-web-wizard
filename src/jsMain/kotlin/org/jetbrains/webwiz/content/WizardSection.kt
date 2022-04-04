@file:Suppress("UNCHECKED_CAST")

package org.jetbrains.webwiz.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.webwiz.generator.ProjectFile
import org.jetbrains.webwiz.generator.generate
import org.jetbrains.webwiz.models.KmpLibrary
import org.jetbrains.webwiz.models.KotlinVersion
import org.jetbrains.webwiz.models.ProjectInfo
import org.jetbrains.webwiz.models.Target
import org.jetbrains.webwiz.setHighlightedCode
import org.jetbrains.webwiz.style.WtContainer
import org.jetbrains.webwiz.style.WtOffsets
import org.jetbrains.webwiz.style.WtSections
import org.jetbrains.webwiz.style.WtTexts
import kotlin.random.Random

private val defaultProject = ProjectInfo(
    projectName = "KMP Project",
    moduleName = "shared",
    packageName = "org.sample.application",
    kotlinVersion = KotlinVersion.Stable,
    targets = setOf(Target.ANDROID, Target.IOS),
    enableTests = true,
    dependencies = setOf(
        KmpLibrary.COROUTINES,
        KmpLibrary.KERMIT_LOGGER,
        KmpLibrary.KTOR_CORE,
        KmpLibrary.KTOR_CLIENT_IOS,
        KmpLibrary.KTOR_CLIENT_ANDROID
    ),
    gradlePlugins = emptySet()
).normalize()

@Composable
fun WizardSection(callback: (projectInfo: ProjectInfo) -> Unit) = Section({
    classes(WtSections.wtSection)
}) {
    Div({
        classes(WtContainer.wtContainer)
    }) {
        val projectInfoState = remember { mutableStateOf(defaultProject) }
        Div({ classes(WtOffsets.rowItems, WtTexts.wtText1) }) {
            Span({ classes(WtOffsets.textInputLabelsStyle) }) {
                Text("Project name")
            }

            TextInput(projectInfoState.value.projectName) {

                onInput { event ->
                    projectInfoState.value = projectInfoState.value.copy(projectName = event.value)
                }

                classes(WtOffsets.textInputStyle)
            }
        }
        Div({ classes(WtOffsets.rowItems, WtTexts.wtText1) }) {
            Span({ classes(WtOffsets.textInputLabelsStyle) }) {
                Text("Module name")
            }

            TextInput(projectInfoState.value.moduleName) {

                onInput { event ->
                    projectInfoState.value = projectInfoState.value.copy(moduleName = event.value)
                }

                classes(WtOffsets.textInputStyle)
            }
        }
        Div({ classes(WtOffsets.rowItems, WtTexts.wtText1) }) {
            Span({ classes(WtOffsets.textInputLabelsStyle) }) {
                Text("Package")
            }
            TextInput(projectInfoState.value.packageName) {
                onInput { event ->
                    projectInfoState.value = projectInfoState.value.copy(packageName = event.value)
                }
                classes(WtOffsets.textInputStyle)
            }
        }
        Div({ classes(WtOffsets.rowItems, WtTexts.wtText1) }) {
            Span({ classes(WtOffsets.textInputLabelsStyle) }) {
                Text("Kotlin version")
            }

            Div {
                KotlinVersionSwitcher(projectInfoState.value.kotlinVersion) {
                    projectInfoState.value = projectInfoState.value.copy(kotlinVersion = it)
                }
            }
        }
        Div({ classes(WtOffsets.rowTargetsItems, WtTexts.wtText1) }) {
            Span({ classes(WtOffsets.textInputLabelsStyle) }) {
                Text("Targets")
            }
            TargetChips(projectInfoState.value) { projectInfoState.value = it }
        }
        Div({ classes(WtOffsets.rowTargetsItems, WtTexts.wtText1) }) {
            Span({ classes(WtOffsets.textInputLabelsStyle) }) {
                Text("Libraries")
            }
            LibrariesChips(projectInfoState.value) { projectInfoState.value = it }
        }
        Div({ classes(WtOffsets.rowTargetsItems, WtTexts.wtText1) }) {
            Span({ classes(WtOffsets.textInputLabelsStyle) }) {
                Text("Plugins")
            }
            PluginsChips(projectInfoState.value) { projectInfoState.value = it }
        }
        Div({ classes(WtOffsets.rowItems, WtTexts.wtText1) }) {
            Span({ classes(WtOffsets.textInputLabelsStyle) }) {
                Text("Include tests")
            }
            Span({ classes(WtOffsets.testsCheckboxStyle) }) {
                CheckboxInput(projectInfoState.value.enableTests) {
                    onChange {
                        projectInfoState.value =
                            projectInfoState.value.copy(enableTests = !projectInfoState.value.enableTests)
                    }
                }
            }
        }

        Div({ classes(WtOffsets.generateButtonStyle, WtTexts.wtText1) }) {
            Button(attrs = {
                classes(WtTexts.wtButton, WtTexts.wtButtonContrast, WtOffsets.wtTopOffset24)
                onClick { callback(projectInfoState.value) }
                style {
                    cursor("pointer")
                }
            }) {
                Text("Download new project")
            }
        }

        Div({
            classes(WtOffsets.wtTopOffset24)
            style {
                backgroundColor(rgba(39, 40, 44, 0.05))
                borderRadius(8.px, 8.px, 8.px)
                property("font-family", "'JetBrains Mono', monospace")
                fontSize(10.pt)
                display(DisplayStyle.Flex)
                flexDirection(FlexDirection.Row)
                minHeight(600.pt)
            }
        }) {
            val selectedFilePath = remember { mutableStateOf("build.gradle.kts") }

            val structure = projectInfoState.value.generate()
            if (structure.none { it.path == selectedFilePath.value }) {
                selectedFilePath.value = "build.gradle.kts"
            }

            Div(attrs = {
                style {
                    color(rgba(39, 40, 44, .7))
                    marginTop(5.px)
                    marginBottom(5.px)
                }
            }) {
                filesStructure(structure, selectedFilePath.value) {
                    selectedFilePath.value = it.path
                }
            }
            Span(attrs = {
                style {
                    width(1.pt)
                    backgroundColor(Color("gray"))
                    marginLeft(5.px)
                    marginRight(5.px)
                }
            })
            Pre(attrs = {
                style {
                    color(rgba(39, 40, 44, .7))
                    lineHeight(29.px)
                    marginTop(5.px)
                    marginBottom(5.px)
                }
            }) {
                Code {
                    val path = selectedFilePath.value
                    val lang = when {
                        path.endsWith(".kt") -> "kotlin"
                        path.endsWith(".kts") -> "gradle"
                        path.endsWith(".xml") -> "xml"
                        else -> "text"
                    }
                    val content = structure.first { it.path == path }.content
                    DomSideEffect(content) {
                        it.setHighlightedCode(content, lang)
                    }
                }
            }
        }
    }
}

@Composable
private fun filesStructure(
    list: List<ProjectFile>,
    selectedFilePath: String,
    onClick: (file: ProjectFile) -> Unit
) {
    fun putFileToDir(f: ProjectFile, path: List<String>, dir: MutableMap<String, Any?>): MutableMap<String, Any?> {
        if (path.size == 1) {
            dir[path.first()] = f
        } else {
            val m = dir.getOrPut(path.first()) { mutableMapOf<String, Any?>() } as MutableMap<String, Any?>
            dir[path.first()] = putFileToDir(f, path.slice(1 until path.size), m)
        }
        return dir
    }

    val root = mutableMapOf<String, Any?>()
    list.forEach { f -> putFileToDir(f, f.path.split('/'), root) }
    fileTree(0, root, selectedFilePath, onClick)
}

@Composable
private fun fileTree(
    level: Int,
    map: Map<String, Any?>,
    selectedFilePath: String,
    onClick: (file: ProjectFile) -> Unit
) {
    Ul(attrs = {
        if (level == 0) classes("filetree")
    }) {
        map.entries.sortedWith(FileEntryComparator).forEach { fileEntry ->
            val name = fileEntry.key
            val content = fileEntry.value as? Map<String, Any?>
            if (content == null) {
                val f = fileEntry.value as ProjectFile
                Li(attrs = {
                    onClick { onClick(f) }
                    style {
                        cursor("pointer")
                    }
                }) {
                    if (selectedFilePath == f.path) {
                        B(attrs = {
                            style {
                                color(Color.black)
                            }
                        }) { Text(name) }
                    } else Text(name)
                }
            } else {
                var flattenName = name
                var dir: Map<String, Any?> = content
                while (dir.entries.size == 1 && dir.entries.first().value is Map<*, *>) {
                    flattenName += "/" + dir.entries.first().key
                    dir = dir.entries.first().value as Map<String, Any?>
                }

                Li {
                    val id = "level-$level-$flattenName-${Random.nextFloat()}"
                    Input(InputType.Checkbox) { id(id) }
                    Label(forId = id, attrs = {
                        style {
                            cursor("pointer")
                        }
                    }) { Text(flattenName) }
                    fileTree(level + 1, dir, selectedFilePath, onClick)
                }
            }
        }
    }
}

private object FileEntryComparator : Comparator<Map.Entry<String, Any?>> {
    override fun compare(a: Map.Entry<String, Any?>, b: Map.Entry<String, Any?>): Int {
        val aIsDir = (a.value as? Map<String, Any?>) != null
        val bIsDir = (b.value as? Map<String, Any?>) != null
        return if (aIsDir == bIsDir) {
            a.key.compareTo(b.key)
        } else {
            if (aIsDir) -1 else 1
        }
    }
}