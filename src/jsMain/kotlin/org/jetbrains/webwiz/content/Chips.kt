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
import org.jetbrains.webwiz.models.ProjectInfo
import org.jetbrains.webwiz.models.Target
import org.jetbrains.webwiz.style.WtOffsets

@Composable
fun TargetChips(projectInfo: ProjectInfo, update: (ProjectInfo) -> Unit) {
    Div({ classes(WtOffsets.targetsCheckboxesListStyle) }) {
        Target.values().forEach { t ->
            val id = "target_checkbox_${t.name}"
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
                    id(id)
                }
                Label(forId = id) { Text(t.userName) }
            }
        }
    }
}

private fun ProjectInfo.applyNewTargets(targets: Set<Target>): ProjectInfo = copy(
    targets = targets,
    dependencies = dependencies.filter { it.canBeApplied(targets) }.toSet(),
    gradlePlugins = gradlePlugins.filter { it.canBeApplied(targets) }.toSet()
)


@Composable
fun LibrariesChips(projectInfo: ProjectInfo, update: (ProjectInfo) -> Unit) {
    Div({ classes(WtOffsets.targetsCheckboxesListStyle) }) {
        KmpLibrary.values().forEach { lib ->
            val id = "lib_checkbox_${lib.name}"
            if (!lib.canBeApplied(projectInfo.targets)) {
                return@forEach DisabledChip(id, lib.userName)
            }

            return@forEach Span({ classes(WtOffsets.targetsCheckboxesStyle) }) {
                CheckboxInput(projectInfo.dependencies.contains(lib)) {
                    onChange { event ->
                        val new: Set<KmpLibrary> = when {
                            event.value -> projectInfo.dependencies.plus(lib)
                            else -> projectInfo.dependencies.minus(lib)
                        }
                        update(projectInfo.copy(dependencies = new))
                    }
                    id(id)
                }
                Label(forId = id) { Text(lib.userName) }
            }
        }
    }
}

@Composable
fun PluginsChips(projectInfo: ProjectInfo, update: (ProjectInfo) -> Unit) {
    Div({ classes(WtOffsets.targetsCheckboxesListStyle) }) {
        GradlePlugin.values().forEach { t ->
            val id = "checkbox_gradle_plugin_${t.name}"
            if (!t.canBeApplied(projectInfo.targets)) {
                return@forEach DisabledChip(id, t.userName)
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
                    id(id)
                }
                Label(forId = id) { Text(t.userName) }
            }
        }
    }
}

@Composable
fun DisabledChip(id: String, label: String) {
    Span({ classes(WtOffsets.targetsCheckboxesStyle) }) {
        CheckboxInput() {
            id(id)
            disabled()
        }

        Label(forId = id) {
            Text(label)
        }
    }
}