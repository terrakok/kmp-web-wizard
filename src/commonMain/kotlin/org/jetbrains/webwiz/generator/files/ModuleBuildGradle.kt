package org.jetbrains.webwiz.generator.files

import org.jetbrains.webwiz.generator.NAN
import org.jetbrains.webwiz.generator.ProjectFile
import org.jetbrains.webwiz.generator.deleteNans
import org.jetbrains.webwiz.models.GradlePlugin
import org.jetbrains.webwiz.models.KmpLibrary
import org.jetbrains.webwiz.models.ProjectInfo
import org.jetbrains.webwiz.models.Target
import org.jetbrains.webwiz.models.isNativeTargetPresent

class ModuleBuildGradle(val projectInfo: ProjectInfo) : ProjectFile {
    override val path = "${projectInfo.moduleName}/build.gradle.kts"
    override val content: String
        get() = """
${generatePluginsBlock()}

${generateKotlinBlock()}

${if (Target.ANDROID in projectInfo.targets) generateAndroidPluginConfig("21", "31") else NAN}
${if (GradlePlugin.APPLICATION in projectInfo.gradlePlugins) generateApplicationPluginConfig() else NAN}
${if (GradlePlugin.PUBLISH in projectInfo.gradlePlugins) generatePublishPluginConfig("0.1") else NAN}
        """.trimIndent().deleteNans()

    private fun generatePluginsBlock(): String = """
plugins {
    kotlin("multiplatform")
    ${if (KmpLibrary.SERIALIZATION in projectInfo.dependencies) "kotlin(\"plugin.serialization\")" else NAN}
    ${if (Target.ANDROID in projectInfo.targets) "id(\"com.android.library\")" else NAN}
    ${if (GradlePlugin.APPLICATION in projectInfo.gradlePlugins) "application" else NAN}
    ${if (GradlePlugin.PUBLISH in projectInfo.gradlePlugins) "`maven-publish`" else NAN}
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
            Target.JVM -> "jvm()" + (if (projectInfo.gradlePlugins.contains(GradlePlugin.APPLICATION)) " { withJava() }" else "")
            Target.JS -> "js()"
            Target.WASM -> "wasm32()"
            Target.ANDROID_NATIVE -> "androidNativeArm64()"
            Target.LINUX -> "linuxX64()"
            Target.MACOS -> "macosX64()\n    /* macosArm64() sure all macos dependencies support this target */"
            Target.IOS -> "iosX64()\n    iosArm64()\n    /* iosSimulatorArm64() sure all ios dependencies support this target */"
            Target.TV_OS -> "tvosX64()\n    tvosArm64()\n    /* tvosSimulatorArm64() sure all tvos dependencies support this target */"
            Target.WATCH_OS -> "watchosX64()\n    watchosArm64()\n    /* watchosSimulatorArm64() sure all watchos dependencies support this target */"
            Target.WINDOWS -> "mingwX64()"
        }
    }

    private fun setupSourceSets() = """
    |sourceSets {
    |        /* Main source sets */
             ${commonMainSourceSet()}
    |        ${leafSourceSets("Main")}
    |        ${sharedSourceSets("Main")}
    |        ${if (projectInfo.targets.isNativeTargetPresent()) nativeSourceSets("Main") else NAN}
    |
    |        /* Main hierarchy */
    |        ${sourceSetsDependencies("Main")}
    |        ${if (projectInfo.targets.isNativeTargetPresent()) nativeSourceSetsDependencies("Main") else NAN}
    |
    |        /* Test source sets */
             ${commonTestSourceSet()}
    |        ${leafSourceSets("Test")}
    |        ${sharedSourceSets("Test")}
    |        ${if (projectInfo.targets.isNativeTargetPresent()) nativeSourceSets("Test") else NAN}
    |
    |        /* Test hierarchy */
    |        ${sourceSetsDependencies("Test")}
    |        ${if (projectInfo.targets.isNativeTargetPresent()) nativeSourceSetsDependencies("Test") else NAN}
    |    }
""".trimMargin().deleteNans()

    private fun commonMainSourceSet(): String {
        val deps = projectInfo.dependencies.map { "implementation(\"${it.dep}\")" }
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

    private fun commonTestSourceSet() =
        """|        val commonTest by getting {
           |            dependencies {
           |                implementation(kotlin("test"))
           |            }
           |        }"""

    private fun leafSourceSets(compilation: String): String {
        val intention = "\n|        "
        return projectInfo.targets.joinToString(intention) {
            when (it) {
                Target.ANDROID -> "val android$compilation by getting"
                Target.JVM -> "val jvm$compilation by getting"
                Target.JS -> "val js$compilation by getting"
                Target.WASM -> "val wasm32$compilation by getting"
                Target.ANDROID_NATIVE -> "val androidNativeArm64$compilation by getting"
                Target.LINUX -> "val linuxX64$compilation by getting"
                Target.MACOS -> "val macosX64$compilation by getting ${intention}/* val macosArm64$compilation by getting */"
                Target.IOS -> "val iosX64$compilation by getting ${intention}val iosArm64$compilation by getting${intention}/* val iosSimulatorArm64$compilation by getting */"
                Target.TV_OS -> "val tvosX64$compilation by getting ${intention}val tvosArm64$compilation by getting ${intention}/* val tvosSimulatorArm64$compilation by getting */"
                Target.WATCH_OS -> "val watchosX64$compilation by getting ${intention}val watchosArm64$compilation by getting  ${intention}/* val watchosSimulatorArm64$compilation by getting */"
                Target.WINDOWS -> "val mingwX64$compilation by getting"
            }
        }
    }

    private fun sharedSourceSets(compilation: String): String =
        projectInfo.targets.joinToString("\n|        ") {
            when (it) {
                Target.ANDROID -> NAN
                Target.JVM -> NAN
                Target.JS -> NAN
                Target.WASM -> "val wasm$compilation by creating"
                Target.ANDROID_NATIVE -> "val androidNative$compilation by creating"
                Target.LINUX -> "val linux$compilation by creating"
                Target.MACOS -> "val macos$compilation by creating"
                Target.IOS -> "val ios$compilation by creating"
                Target.TV_OS -> "val tvos$compilation by creating"
                Target.WATCH_OS -> "val watchos$compilation by creating"
                Target.WINDOWS -> "val windows$compilation by creating"
            }
        }

    private fun sourceSetsDependencies(compilation: String): String {
        val intention = "\n|        "
        return projectInfo.targets.joinToString(intention) {
            when (it) {
                Target.ANDROID -> "android$compilation.dependsOn(common$compilation)"
                Target.JVM -> "jvm$compilation.dependsOn(common$compilation)"
                Target.JS -> "js$compilation.dependsOn(common$compilation)"
                Target.WASM -> "wasm$compilation.dependsOn(native$compilation)${intention}wasm32$compilation.dependsOn(wasm$compilation)"
                Target.ANDROID_NATIVE -> "androidNative$compilation.dependsOn(native$compilation) ${intention}androidNativeArm64$compilation.dependsOn(androidNative$compilation)"
                Target.LINUX -> "linux$compilation.dependsOn(native$compilation)${intention}linuxX64$compilation.dependsOn(linux$compilation)"
                Target.MACOS -> "macos$compilation.dependsOn(native$compilation)${intention}macosX64$compilation.dependsOn(macos$compilation)${intention}/* macosArm64$compilation.dependsOn(macos$compilation) */"
                Target.IOS -> "ios$compilation.dependsOn(native$compilation)${intention}iosX64$compilation.dependsOn(ios$compilation)${intention}iosArm64$compilation.dependsOn(ios$compilation)${intention}/* iosSimulatorArm64$compilation.dependsOn(ios$compilation) */"
                Target.TV_OS -> "tvos$compilation.dependsOn(native$compilation)${intention}tvosX64$compilation.dependsOn(tvos$compilation)${intention}tvosArm64$compilation.dependsOn(tvos$compilation)${intention}/* tvosSimulatorArm64$compilation.dependsOn(tvos$compilation) */"
                Target.WATCH_OS -> "watchos$compilation.dependsOn(native$compilation)${intention}watchosX64$compilation.dependsOn(watchos$compilation)${intention}watchosArm64$compilation.dependsOn(watchos$compilation)${intention}/* watchosSimulatorArm64$compilation.dependsOn(watchos$compilation) */"
                Target.WINDOWS -> "windows$compilation.dependsOn(native$compilation)${intention}mingwX64$compilation.dependsOn(windows$compilation)"
            }
        }
    }

    private fun nativeSourceSets(compilation: String) = "val native$compilation by creating"
    private fun nativeSourceSetsDependencies(compilation: String) = "native$compilation.dependsOn(common$compilation)"

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
//required for maven publication
group = "${projectInfo.packageName}"
version = "$version"

""".trimIndent()

}