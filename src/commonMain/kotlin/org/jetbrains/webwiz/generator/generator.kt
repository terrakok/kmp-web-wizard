package org.jetbrains.webwiz.generator

import org.jetbrains.webwiz.generator.files.*
import org.jetbrains.webwiz.models.*
import org.jetbrains.webwiz.models.Target.*

interface ProjectFile {
    val path: String
    val content: String
}

fun ProjectInfo.generate(moduleName: String = "shared"): List<ProjectFile> = mutableListOf<ProjectFile>().apply {
    val info = this@generate.normalize()

    add(Gitignore())
    add(Gradlew())
    add(GradleBat())
    add(GradleWrapperProperties("7.1"))
    add(GradleWrapperJar())

    add(RootBuildGradle(info))
    add(SettingsGradle(moduleName))
    add(GradleProperties())

    add(ModuleBuildGradle(moduleName, info))

    add(CommonPlatformKt(moduleName, info))
    info.targets.forEach { target ->
        add(TargetPlatformKt(moduleName, target, info))
    }
    if (info.targets.isNativeTargetPresent()) {
        add(IntermediatePlatformKt(moduleName, "native", info))
    }

    if (info.gradlePlugins.contains(GradlePlugin.APPLICATION)) {
        add(ApplicationKt(moduleName, info))
    }

    if (info.targets.contains(ANDROID)) {
        add(AndroidManifestXml(moduleName, info))
    }

    if (info.enableTests) {
        add(CommonPlatformTestKt(moduleName, info))
        info.targets.forEach { target ->
            add(TargetPlatformTestKt(moduleName, target, info))
        }
        if (info.targets.isNativeTargetPresent()) {
            add(IntermediatePlatformTestKt(moduleName, "native", info))
        }
    }
}

internal const val NAN = "®®®"
internal fun String.deleteNans(): String =
    lines().filter { !it.contains(NAN) }.joinToString("\n")

