package org.jetbrains.webwiz.content

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import org.jetbrains.compose.common.foundation.layout.Row
import org.jetbrains.compose.common.ui.ExperimentalComposeWebWidgetsApi
import org.jetbrains.compose.web.attributes.InputType
import org.jetbrains.compose.web.css.*
import org.jetbrains.compose.web.dom.*
import org.jetbrains.webwiz.generator.ProjectFile
import org.jetbrains.webwiz.generator.files.RootBuildGradle
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
    enableTests = false,
    dependencies = setOf(KmpLibrary.SERIALIZATION),
    gradlePlugins = emptySet()
).normalize()

internal val projectInfoState = mutableStateOf(defaultProject)

val switcherState = mutableStateOf(mapOf(KotlinVersion.EAP to false, KotlinVersion.Stable to true))

@Composable
@OptIn(ExperimentalComposeWebWidgetsApi::class)
fun WizardSection(callback: (projectInfo: ProjectInfo) -> Unit) = Section({
    classes(WtSections.wtSection)
}) {
    Div({
        classes(WtContainer.wtContainer)
    }) {
        Div({
            classes(WtTexts.wtText1)
        }) {
            Row {
                Div({ classes(WtOffsets.rowItems) }) {
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
            }
            Row {
                Div({ classes(WtOffsets.rowItems) }) {
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
            }
            Row {
                Div({ classes(WtOffsets.rowItems) }) {
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
            }
            Row {
                Div({ classes(WtOffsets.rowItems) }) {
                    Span({ classes(WtOffsets.textInputLabelsStyle) }) {
                        Text("Kotlin version")
                    }

                    Div({ classes(WtOffsets.kotlinSwitcherStyle) }) {
                        KotlinVersionSwitcher(
                            KotlinVersion.values(),
                            switcherState.value
                        )
                        {
                            projectInfoState.value = projectInfoState.value.copy(kotlinVersion = it)
                            switcherState.value = mapOf(
                                KotlinVersion.EAP to (
                                    when (it) {
                                        KotlinVersion.EAP -> !switcherState.value[KotlinVersion.EAP]!!
                                        KotlinVersion.Stable -> switcherState.value[KotlinVersion.Stable]!!
                                    }),
                                KotlinVersion.Stable to (
                                    when (it) {
                                        KotlinVersion.EAP -> switcherState.value[KotlinVersion.EAP]!!
                                        KotlinVersion.Stable -> !switcherState.value[KotlinVersion.Stable]!!
                                    })
                            )
                        }
                    }
                }
            }
            Row {
                Div({ classes(WtOffsets.rowItems) }) {
                    Div({ classes(WtOffsets.rowTargetsItems) }) {
                        Span({ classes(WtOffsets.textInputLabelsStyle) }) {
                            Text("Targets")
                        }
                        TargetChips()
                    }
                }
            }

            Row {
                Div({ classes(WtOffsets.rowTargetsItems) }) {
                    Span({ classes(WtOffsets.textInputLabelsStyle) }) {
                        Text("Libraries")
                    }
                    LibrariesChips()
                }
            }

            Row {
                Div({ classes(WtOffsets.rowTargetsItems) }) {
                    Span({ classes(WtOffsets.textInputLabelsStyle) }) {
                        Text("Plugins")
                    }
                    PluginsChips()
                }
            }

            Row {
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

            Div({ classes(WtOffsets.generateButtonStyle) }) {
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
                    padding(12.px, 16.px)
                    property("font-family", "'JetBrains Mono', monospace")
                    fontSize(10.pt)
                    display(DisplayStyle.Flex)
                    flexDirection(FlexDirection.Row)
                    minHeight(600.pt)
                }
            }) {
                val structure = projectInfoState.value.generate()
                val selectedFile = remember(projectInfoState.value) { mutableStateOf(structure.first { it is RootBuildGradle }) }

                filesStructure(structure, selectedFile)
                Span(attrs = {
                    style {
                        width(1.pt)
                        backgroundColor(Color("gray"))
                        marginLeft(5.px)
                        marginRight(5.px)
                    }
                })
                selectedFile.value.let { f ->
                    val lang = when {
                        f.path.endsWith(".kt") -> "kotlin"
                        f.path.endsWith(".kts") -> "gradle"
                        f.path.endsWith(".xml") -> "xml"
                        else -> "text"
                    }
                    Pre(attrs = {
                        style {
                            overflow("auto")
                        }
                    }) {
                        Code({
                            classes("language-$lang", "hljs")
                            style {
                                property("tab-size", 4)
                                backgroundColor(Color("transparent"))
                            }
                        }) {
                            DomSideEffect(f.content) {
                                it.setHighlightedCode(f.content)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun filesStructure(list: List<ProjectFile>, selectedFile: MutableState<ProjectFile>) {
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
    fileTree(0, root, selectedFile)
}

@Composable
private fun fileTree(level: Int, map: Map<String, Any?>, selectedFile: MutableState<ProjectFile>) {
    Ul(attrs = {
        if (level == 0) {
            classes("filetree")
            style {
                width(200.pt)
                paddingLeft(0.px)
            }
        } else {
            style {
                paddingLeft(10.px)
            }
        }
    }) {
        map.entries.sortedWith(FileEntryComparator).forEach { fileEntry ->
            val name = fileEntry.key
            val content = fileEntry.value as? Map<String, Any?>
            if (content == null) {
                val f = fileEntry.value as ProjectFile
                Li(attrs = {
                    onClick { selectedFile.value = f }
                    style {
                        cursor("pointer")
                    }
                }) {
                    if (selectedFile.value.path == f.path) B { Text(name) }
                    else Text(name)
                }
            } else {
                Li {
                    val id = "level-$level-$name-${Random.nextFloat()}"
                    Input(InputType.Checkbox) { id(id) }
                    Label(forId = id, attrs = {
                        style {
                            cursor("pointer")
                        }
                    }) { Text(name) }
                    fileTree(level + 1, content, selectedFile)
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