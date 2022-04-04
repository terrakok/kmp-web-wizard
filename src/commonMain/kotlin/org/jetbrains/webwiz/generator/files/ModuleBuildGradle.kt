package org.jetbrains.webwiz.generator.files

import org.jetbrains.webwiz.generator.NAN
import org.jetbrains.webwiz.generator.ProjectFile
import org.jetbrains.webwiz.generator.deleteNans
import org.jetbrains.webwiz.models.GradlePlugin
import org.jetbrains.webwiz.models.KmpLibrary
import org.jetbrains.webwiz.models.ProjectInfo
import org.jetbrains.webwiz.models.SourceSetDelegate
import org.jetbrains.webwiz.models.SourceSetDelegate.CREATING
import org.jetbrains.webwiz.models.SourceSetDelegate.GETTING
import org.jetbrains.webwiz.models.SourceSetType.MAIN
import org.jetbrains.webwiz.models.Target
import org.jetbrains.webwiz.models.isCommonNativeTargetPresent
import org.jetbrains.webwiz.models.isNativeTargetPresent

class ModuleBuildGradle(val projectInfo: ProjectInfo) : ProjectFile {
    override val path = "${projectInfo.moduleName}/build.gradle.kts"
    override val content: String
        get() = """
${generatePluginsBlock()}

${if (GradlePlugin.PUBLISH in projectInfo.gradlePlugins) generatePublishPluginConfig("0.1") else NAN}
${generateKotlinBlock()}

${if (Target.ANDROID in projectInfo.targets) generateAndroidPluginConfig("21", "31") else NAN}
${if (GradlePlugin.APPLICATION in projectInfo.gradlePlugins) generateApplicationPluginConfig() else NAN}
        """.trimIndent().deleteNans()

    private fun generatePluginsBlock(): String = """
plugins {
    kotlin("multiplatform")
    ${if (KmpLibrary.SERIALIZATION in projectInfo.dependencies) "kotlin(\"plugin.serialization\")" else NAN}
    ${if (Target.ANDROID in projectInfo.targets) "id(\"com.android.library\")" else NAN}
    ${if (GradlePlugin.APPLICATION in projectInfo.gradlePlugins) "application" else NAN}
    ${if (GradlePlugin.PUBLISH in projectInfo.gradlePlugins) "`maven-publish`" else NAN}
    ${if (GradlePlugin.SQL_DELIGHT in projectInfo.gradlePlugins) "id(\"com.squareup.sqldelight\")" else NAN}
    ${if (GradlePlugin.REALM in projectInfo.gradlePlugins) "id(\"io.realm.kotlin\")" else NAN}
}
""".trimIndent().deleteNans()

    private fun generateKotlinBlock() = """
kotlin {
    ${registerTargets(projectInfo.targets)}

    ${setupSourceSets()}
}
    """.trimIndent()

    private fun registerTargets(
        targets: Set<Target>
    ) = targets.joinToString("\n    ") {
        when (it) {
            Target.ANDROID -> "android()"
            Target.JVM -> if (projectInfo.gradlePlugins.contains(GradlePlugin.APPLICATION)) "jvm {\n        withJava()\n    }" else "jvm()"
            Target.JS -> "js {\n        browser()\n        nodejs()\n    }"
            Target.LINUX -> "linuxX64()"
            Target.MACOS -> "macosX64()\n    macosArm64()"
            Target.IOS -> "iosX64()\n    iosArm64()\n    iosSimulatorArm64()"
            Target.TV_OS -> "tvosX64()\n    tvosArm64()\n    tvosSimulatorArm64()"
            Target.WATCH_OS -> "watchosX64()\n    watchosArm64()\n    watchosSimulatorArm64()"
            Target.WINDOWS -> "mingwX64()"
        }
    }

    private fun setupSourceSets() = """
    |sourceSets {
    |        /* Main source sets */
             ${commonMainSourceSet()}
    |        ${commonNativeSourceSet("Main")}
    |        ${platformSourceSets("Main")}
    |        ${targetSourceSets("Main")}
    |
    |        /* Main hierarchy */
    |        ${sourceSetsHierarchy("Main")}
    |
    |        /* Test source sets */
             ${commonTestSourceSet()}
    |        ${commonNativeSourceSet("Test")}
    |        ${platformSourceSets("Test")}
    |        ${targetSourceSets("Test")}
    |
    |        /* Test hierarchy */
    |        ${sourceSetsHierarchy("Test")}
    |    }
""".trimMargin().deleteNans()

    private fun commonMainSourceSet(): String {
        val deps = projectInfo.dependencies.filter {
            it.sourceSetType.sourceSetTypeName == MAIN.sourceSetTypeName
        }.map { "implementation(\"${it.dep}\")" }
        return if (deps.isEmpty()) {
            "  |        val commonMain by getting"
        } else {
            """|        val commonMain by getting {
               |            dependencies {
               |                ${deps.joinToString("\n|                ")}
               |            }
               |        }"""
        }
    }

    private fun singleSourceSet(
        target: Target,
        compilation: String,
        sourceSetDelegate: SourceSetDelegate
    ): String {
        val deps = projectInfo.singleTargetDependencies
            .filter { it.target == target && it.sourceSetType.sourceSetTypeName == compilation }
            .map { "implementation(\"${it.dep}\")" }
        return if (deps.isEmpty()) {
            "val ${target.targetName}$compilation by ${sourceSetDelegate.delegate}"
        } else {
            """val ${target.targetName}$compilation by ${sourceSetDelegate.delegate} {
               |            dependencies {
               |                ${deps.joinToString("\n|                ")}
               |            }
               |        }"""
        }
    }

    private fun commonTestSourceSet() =
        """|        val commonTest by getting {
           |            dependencies {
           |                implementation(kotlin("test"))
           |            }
           |        }"""

    private fun targetSourceSets(compilation: String): String {
        val intention = "\n|        "
        return projectInfo.targets.joinToString(intention) {
            when (it) {
                Target.ANDROID -> singleSourceSet(Target.ANDROID, compilation, GETTING)
                Target.JVM -> singleSourceSet(Target.JVM, compilation, GETTING)
                Target.JS -> singleSourceSet(Target.JS, compilation, GETTING)
                Target.LINUX -> "val linuxX64$compilation by getting"
                Target.MACOS -> "val macosX64$compilation by getting ${intention}val macosArm64$compilation by getting"
                Target.IOS -> "val iosX64$compilation by getting ${intention}val iosArm64$compilation by getting${intention}val iosSimulatorArm64$compilation by getting"
                Target.TV_OS -> "val tvosX64$compilation by getting ${intention}val tvosArm64$compilation by getting ${intention}val tvosSimulatorArm64$compilation by getting"
                Target.WATCH_OS -> "val watchosX64$compilation by getting ${intention}val watchosArm64$compilation by getting  ${intention}val watchosSimulatorArm64$compilation by getting"
                Target.WINDOWS -> "val mingwX64$compilation by getting"
            }
        }
    }

    private fun platformSourceSets(compilation: String): String =
        projectInfo.targets.joinToString("\n|        ") {
            when (it) {
                Target.ANDROID, Target.JVM, Target.JS -> NAN
                Target.LINUX, Target.MACOS, Target.TV_OS, Target.WATCH_OS, Target.WINDOWS, Target.IOS ->
                    singleSourceSet(it, compilation, CREATING)
            }
        }

    private fun sourceSetsHierarchy(compilation: String): String {
        val intention = "\n|        "

        val commonNative = projectInfo.targets.isCommonNativeTargetPresent()
        val nativeParent = if (commonNative) "native$compilation" else "common$compilation"
        val nativeSourceSet = if (commonNative) "native$compilation.dependsOn(common$compilation)$intention" else ""

        return nativeSourceSet + projectInfo.targets.joinToString(intention) {
            when (it) {
                Target.ANDROID -> "android$compilation.dependsOn(common$compilation)"
                Target.JVM -> "jvm$compilation.dependsOn(common$compilation)"
                Target.JS -> "js$compilation.dependsOn(common$compilation)"
                Target.LINUX -> "linux$compilation.dependsOn($nativeParent)${intention}linuxX64$compilation.dependsOn(linux$compilation)"
                Target.MACOS -> "macos$compilation.dependsOn($nativeParent)${intention}macosX64$compilation.dependsOn(macos$compilation)${intention}macosArm64$compilation.dependsOn(macos$compilation)"
                Target.IOS -> "ios$compilation.dependsOn($nativeParent)${intention}iosX64$compilation.dependsOn(ios$compilation)${intention}iosArm64$compilation.dependsOn(ios$compilation)${intention}iosSimulatorArm64$compilation.dependsOn(ios$compilation)"
                Target.TV_OS -> "tvos$compilation.dependsOn($nativeParent)${intention}tvosX64$compilation.dependsOn(tvos$compilation)${intention}tvosArm64$compilation.dependsOn(tvos$compilation)${intention}tvosSimulatorArm64$compilation.dependsOn(tvos$compilation)"
                Target.WATCH_OS -> "watchos$compilation.dependsOn($nativeParent)${intention}watchosX64$compilation.dependsOn(watchos$compilation)${intention}watchosArm64$compilation.dependsOn(watchos$compilation)${intention}watchosSimulatorArm64$compilation.dependsOn(watchos$compilation)"
                Target.WINDOWS -> "windows$compilation.dependsOn($nativeParent)${intention}mingwX64$compilation.dependsOn(windows$compilation)"
            }
        }
    }

    private fun commonNativeSourceSet(compilation: String): String {
        if (!projectInfo.targets.isCommonNativeTargetPresent()) return NAN
        val deps = projectInfo.commonNativeTargetLibraries
            .filter { it.sourceSetType.sourceSetTypeName == compilation }
            .map { "implementation(\"${it.dep}\")" }
        return if (deps.isEmpty()) {
            "val native$compilation by ${CREATING.delegate}"
        } else {
            """val native$compilation by ${CREATING.delegate} {
               |            dependencies {
               |                ${deps.joinToString("\n|                ")}
               |            }
               |        }"""
        }
    }

    private fun generateAndroidPluginConfig(minSdk: String, compileSdk: String) = """
android {
    compileSdk = $compileSdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = $minSdk
        targetSdk = $compileSdk
    }
}

""".trimIndent()

    private fun generateApplicationPluginConfig() = """
application {
    mainClass.set("${projectInfo.packageName}.ApplicationKt")
}

""".trimIndent()

    private fun generatePublishPluginConfig(version: String) = """
/* required for maven publication */
group = "${projectInfo.packageName}"
version = "$version"

""".trimIndent()

}