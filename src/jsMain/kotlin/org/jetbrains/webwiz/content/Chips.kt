package org.jetbrains.webwiz.content

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.CheckboxInput
import org.jetbrains.compose.web.dom.Div
import org.jetbrains.compose.web.dom.Label
import org.jetbrains.compose.web.dom.Span
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.webwiz.models.GradlePlugin
import org.jetbrains.webwiz.models.KmpLibrary
import org.jetbrains.webwiz.models.NativeTargetLibrary
import org.jetbrains.webwiz.models.ProjectInfo
import org.jetbrains.webwiz.models.SingleTargetLibrary
import org.jetbrains.webwiz.models.Target
import org.jetbrains.webwiz.models.isCommonNativeTargetPresent
import org.jetbrains.webwiz.style.WtOffsets

@Composable
fun TargetChips(projectInfo: ProjectInfo, update: (ProjectInfo) -> Unit) {
    Div({ classes(WtOffsets.targetsCheckboxesListStyle) }) {
        Target.values().forEach { t ->
            Span({ classes(WtOffsets.targetsCheckboxesStyle) }) {
                CheckboxInput(projectInfo.targets.contains(t)) {
                    onChange { event ->
                        val new: Set<Target> = when {
                            event.value -> projectInfo.targets.plus(t)
                            projectInfo.targets.size > 1 -> projectInfo.targets.minus(t)
                            else -> projectInfo.targets
                        }
                        update(projectInfo.applyNewTargets(new))
                    }
                    id("checkbox_${t.name}")
                }
                Label(forId = "checkbox_${t.name}") {
                    Text(t.userName)
                }
            }
        }
    }
}

private fun ProjectInfo.applyNewTargets(targets: Set<Target>): ProjectInfo {
    val currentLibraries = dependencies.toMutableSet()
    val currentPlugins = gradlePlugins.toMutableSet()

    for (library in KmpLibrary.values()) {
        if (library.targets != null && targets.any { it !in library.targets }) {
            currentLibraries.remove(library)
        }
    }

    for (plugin in GradlePlugin.values()) {
        if (targets.containsAll(plugin.mandatory) && !targets.any { it in plugin.forbidden })
            continue
        if (plugin.mandatory.isNotEmpty() && targets.any { !plugin.mandatory.contains(it) }) {
            currentPlugins.remove(plugin)
        }
    }

    return copy(
        targets = targets,
        dependencies = currentLibraries,
        gradlePlugins = currentPlugins
    )
}


@Composable
fun LibrariesChips(projectInfo: ProjectInfo, update: (ProjectInfo) -> Unit) {
    Div({ classes(WtOffsets.targetsCheckboxesListStyle) }) {
        KmpLibrary.values().forEach { t ->

            if (t.targets != null && projectInfo.targets.any { it !in t.targets }) {
                return@forEach DisabledChip(t.userName)
            }

            return@forEach Span({ classes(WtOffsets.targetsCheckboxesStyle) }) {
                CheckboxInput(projectInfo.dependencies.contains(t)) {
                    onChange { event ->
                        val new: Set<KmpLibrary> = when {
                            event.value -> projectInfo.dependencies.plus(t)
                            else -> projectInfo.dependencies.minus(t)
                        }
                        update(projectInfo.copy(dependencies = new))
                    }
                    id("checkbox_${t.name}")
                }
                Label(forId = "checkbox_${t.name}") {
                    Text(t.userName)
                }
            }
        }
    }
}

@Composable
fun SingleTargetLibraryChips(projectInfo: ProjectInfo, update: (ProjectInfo) -> Unit) {
    Div({ classes(WtOffsets.targetsCheckboxesListStyle) }) {
        SingleTargetLibrary.values().forEach { t ->

            if (t.target !in projectInfo.targets ) {
                return@forEach DisabledChip(t.userName)
            }

            return@forEach Span({ classes(WtOffsets.targetsCheckboxesStyle) }) {
                CheckboxInput(projectInfo.singleTargetDependencies.contains(t)) {
                    onChange { event ->
                        val new: Set<SingleTargetLibrary> = when {
                            event.value -> projectInfo.singleTargetDependencies.plus(t)
                            else -> projectInfo.singleTargetDependencies.minus(t)
                        }
                        update(projectInfo.copy(singleTargetDependencies = new))
                    }
                    id("checkbox_${t.name}")
                }
                Label(forId = "checkbox_${t.name}") {
                    Text(t.userName)
                }
            }
        }
    }
}

@Composable
fun NativeTargetLibraryChips(projectInfo: ProjectInfo, update: (ProjectInfo) -> Unit) {
    Div({ classes(WtOffsets.targetsCheckboxesListStyle) }) {
        NativeTargetLibrary.values().forEach { t ->

            if (!projectInfo.targets.isCommonNativeTargetPresent() ) {
                return@forEach DisabledChip(t.userName)
            }

            return@forEach Span({ classes(WtOffsets.targetsCheckboxesStyle) }) {
                CheckboxInput(projectInfo.nativeTargetLibraries.contains(t)) {
                    onChange { event ->
                        val new: Set<NativeTargetLibrary> = when {
                            event.value -> projectInfo.nativeTargetLibraries.plus(t)
                            else -> projectInfo.nativeTargetLibraries.minus(t)
                        }
                        update(projectInfo.copy(nativeTargetLibraries = new))
                    }
                    id("checkbox_${t.name}")
                }
                Label(forId = "checkbox_${t.name}") {
                    Text(t.userName)
                }
            }
        }
    }
}

@Composable
fun PluginsChips(projectInfo: ProjectInfo, update: (ProjectInfo) -> Unit) {
    Div({ classes(WtOffsets.targetsCheckboxesListStyle) }) {
        GradlePlugin.values().forEach { t ->
            if (t.mandatory.any { !projectInfo.targets.contains(it) } ||
                t.forbidden.any { projectInfo.targets.contains(it) }) {
                return@forEach DisabledChip(t.userName)
            }

            return@forEach Span({ classes(WtOffsets.targetsCheckboxesStyle) }) {
                CheckboxInput(projectInfo.gradlePlugins.contains(t)) {
                    onChange { event ->
                        val new: Set<GradlePlugin> = when {
                            event.value -> projectInfo.gradlePlugins.plus(t)
                            else -> projectInfo.gradlePlugins.minus(t)
                        }
                        update(projectInfo.copy(gradlePlugins = new))
                    }
                    id("checkbox_gradle_plugin_${t.name}")
                }
                Label(forId = "checkbox_gradle_plugin_${t.name}") {
                    Text(t.userName)
                }
            }
        }
    }
}

@Composable
fun DisabledChip(label: String) {
    Span({ classes(WtOffsets.targetsCheckboxesStyle) }) {
        CheckboxInput() {
            id("checkbox_disabled_$label")
            disabled()
        }

        Label(forId = "checkbox_disabled_$label") {
            Text(label)
        }
    }
}