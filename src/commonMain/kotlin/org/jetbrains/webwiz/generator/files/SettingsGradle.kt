package org.jetbrains.webwiz.generator.files

import org.jetbrains.webwiz.generator.ProjectFile

class SettingsGradle(val moduleName: String) : ProjectFile {
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

include(":$moduleName")
    """.trimIndent()
}