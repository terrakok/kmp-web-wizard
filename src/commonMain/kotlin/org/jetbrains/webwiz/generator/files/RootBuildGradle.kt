package org.jetbrains.webwiz.generator.files

import org.jetbrains.webwiz.generator.ProjectFile
import org.jetbrains.webwiz.models.GradlePlugin
import org.jetbrains.webwiz.models.KmpLibrary
import org.jetbrains.webwiz.models.ProjectInfo
import org.jetbrains.webwiz.models.Target

class RootBuildGradle(val projectInfo: ProjectInfo) : ProjectFile {
    override val path = "build.gradle.kts"
    override val content: String
        get() = buildString {
            appendLine("plugins {")
            appendLine("    kotlin(\"multiplatform\").version(\"${projectInfo.kotlinVersion.versionName}\").apply(false)")
            if (projectInfo.dependencies.contains(KmpLibrary.SERIALIZATION)) {
                appendLine("    kotlin(\"plugin.serialization\").version(\"${projectInfo.kotlinVersion.versionName}\").apply(false)")
            }
            if (projectInfo.targets.contains(Target.ANDROID)) {
                appendLine("    id(\"com.android.library\").version(\"7.2.0\").apply(false)")
            }
            if (GradlePlugin.SQL_DELIGHT in projectInfo.gradlePlugins) {
                appendLine("    id(\"com.squareup.sqldelight\").version(\"1.5.3\").apply(false)")
            }
            if (GradlePlugin.REALM in projectInfo.gradlePlugins) {
                appendLine("    id(\"io.realm.kotlin\").version(\"0.9.0\").apply(false)")
            }
            appendLine("}")
        }
}