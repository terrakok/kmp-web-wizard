package org.jetbrains.webwiz.content

import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.dom.*
import org.jetbrains.webwiz.models.GradlePlugin
import org.jetbrains.webwiz.models.KmpLibrary
import org.jetbrains.webwiz.models.Target
import org.jetbrains.webwiz.style.WtOffsets

@Composable
fun TargetChips() {
    Div({ classes(WtOffsets.targetsCheckboxesListStyle) }) {
        Target.values().forEach { t ->
            Span({ classes(WtOffsets.targetsCheckboxesStyle) }) {
                CheckboxInput(projectInfoState.value.targets.contains(t)) {
                    onChange { event ->
                        val current = projectInfoState.value.targets.toMutableSet()
                        val new: Set<Target> = when {
                            event.value -> current.plus(t)
                            current.size > 1 -> current.minus(t)
                            else -> current
                        }
                        applyTargetsUpdate(targets = new)
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
fun LibrariesChips() {
    Div({ classes(WtOffsets.targetsCheckboxesListStyle) }) {
        KmpLibrary.values().forEach { t ->

            if (t.targets != null && projectInfoState.value.targets.any { it !in t.targets }) {
                return@forEach DisabledChip(t.userName)
            }

            return@forEach Span({ classes(WtOffsets.targetsCheckboxesStyle) }) {
                CheckboxInput(projectInfoState.value.dependencies.contains(t)) {
                    onChange { event ->
                        val current = projectInfoState.value.dependencies.toMutableSet()
                        val new: Set<KmpLibrary> = when {
                            event.value -> current.plus(t)
                            else -> current.minus(t)
                        }
                        projectInfoState.value = projectInfoState.value.copy(dependencies = new)
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
fun PluginsChips() {
    Div({ classes(WtOffsets.targetsCheckboxesListStyle) }) {
        GradlePlugin.values().forEach { t ->
            val targets = projectInfoState.value.targets

            if (t.mandatory.any { !targets.contains(it) } || t.forbidden.any { targets.contains(it) }) {
                return@forEach DisabledChip(t.userName)
            }

            return@forEach Span({ classes(WtOffsets.targetsCheckboxesStyle) }) {
                CheckboxInput(projectInfoState.value.gradlePlugins.contains(t)) {
                    onChange { event ->
                        val current = projectInfoState.value.gradlePlugins.toMutableSet()
                        val new: Set<GradlePlugin> = when {
                            event.value -> current.plus(t)
                            else -> current.minus(t)
                        }
                        projectInfoState.value = projectInfoState.value.copy(gradlePlugins = new)
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

private fun applyTargetsUpdate(targets: Set<Target>) {
    val currentLibraries = projectInfoState.value.dependencies.toMutableSet()
    val currentPlugins = projectInfoState.value.gradlePlugins.toMutableSet()

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

    projectInfoState.value = projectInfoState.value.copy(
        targets = targets,
        dependencies = currentLibraries,
        gradlePlugins = currentPlugins
    )
}