package org.jetbrains.webwiz.generator.files

import org.jetbrains.webwiz.generator.NAN
import org.jetbrains.webwiz.generator.ProjectFile
import org.jetbrains.webwiz.generator.deleteNans

class SettingsGradle(
    val projectName: String,
    val moduleName: String,
    val addDevMaven: Boolean
) : ProjectFile {
    override val path = "settings.gradle.kts"
    override val content: String
        get() = """
pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
        ${if (addDevMaven) "maven(\"https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/\")" else NAN}
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        ${if (addDevMaven) "maven(\"https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/\")" else NAN}
    }
}

rootProject.name = "${projectName.replace(' ', '_')}"
include(":$moduleName")
    """.trimIndent().deleteNans()
}