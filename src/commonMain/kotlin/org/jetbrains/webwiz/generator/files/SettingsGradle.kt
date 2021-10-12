package org.jetbrains.webwiz.generator.files

import org.jetbrains.webwiz.generator.ProjectFile

class SettingsGradle(val projectName: String, val moduleName: String) : ProjectFile {
    override val path = "settings.gradle.kts"
    override val content: String
        get() = """
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "${projectName.replace(' ', '_')}"
include(":$moduleName")
    """.trimIndent()
}