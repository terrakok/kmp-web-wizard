package org.jetbrains.webwiz.generator

import org.jetbrains.webwiz.generator.files.*
import org.jetbrains.webwiz.models.GradlePlugin
import org.jetbrains.webwiz.models.ProjectInfo
import org.jetbrains.webwiz.models.Target.ANDROID
import org.jetbrains.webwiz.models.isCommonNativeTargetPresent

interface ProjectFile {
    val path: String
    val content: String
}

fun ProjectInfo.generate(): List<ProjectFile> = mutableListOf<ProjectFile>().apply {
    val info = this@generate.normalize()

    add(Gitignore())
    add(Gradlew())
    add(GradleBat())
    add(GradleWrapperProperties("7.4.2"))
    add(GradleWrapperJar())

    add(RootBuildGradle(info))
    add(SettingsGradle(projectName, moduleName))
    add(GradleProperties())

    add(ModuleBuildGradle(info))

    add(CommonPlatformKt(info))
    info.targets.forEach { target ->
        add(TargetPlatformKt(target, info))
    }
    if (info.isCommonNativeTargetPresent()) {
        add(IntermediatePlatformKt("native", info))
    }

    if (info.gradlePlugins.contains(GradlePlugin.APPLICATION)) {
        add(ApplicationKt(info))
    }

    if (info.targets.contains(ANDROID)) {
        add(AndroidManifestXml(info))
    }

    if (info.enableTests) {
        add(CommonPlatformTestKt(info))
        info.targets.forEach { target ->
            add(TargetPlatformTestKt(target, info))
        }
        if (info.isCommonNativeTargetPresent()) {
            add(IntermediatePlatformTestKt("native", info))
        }
    }
}

internal const val NAN = "®®®"
internal fun String.deleteNans(): String =
    lines().filter { !it.contains(NAN) }.joinToString("\n")

