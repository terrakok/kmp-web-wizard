package org.jetbrains.webwiz.generator.files

import org.jetbrains.webwiz.generator.NAN
import org.jetbrains.webwiz.generator.ProjectFile
import org.jetbrains.webwiz.generator.deleteNans
import org.jetbrains.webwiz.models.GradlePlugin
import org.jetbrains.webwiz.models.KmpLibrary
import org.jetbrains.webwiz.models.KotlinVersion
import org.jetbrains.webwiz.models.ProjectInfo
import org.jetbrains.webwiz.models.Target

class RootBuildGradle(val projectInfo: ProjectInfo) : ProjectFile {
    override val path = "build.gradle.kts"
    override val content: String
        get() =
            """
buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        ${if (projectInfo.kotlinVersion == KotlinVersion.Dev) "maven(\"https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/\")" else NAN}
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:${projectInfo.kotlinVersion.versionName}")
        ${if (projectInfo.dependencies.contains(KmpLibrary.SERIALIZATION)) "classpath(\"org.jetbrains.kotlin:kotlin-serialization:${projectInfo.kotlinVersion.versionName}\")" else NAN}
        ${if (projectInfo.targets.contains(Target.ANDROID)) "classpath(\"com.android.tools.build:gradle:7.1.1\")" else NAN}
        ${if (GradlePlugin.SQL_DELIGHT in projectInfo.gradlePlugins) "classpath(\"com.squareup.sqldelight:gradle-plugin:1.5.3\")" else NAN}
        ${if (GradlePlugin.REALM in projectInfo.gradlePlugins) "classpath(\"io.realm.kotlin:gradle-plugin:0.9.0\")" else NAN}
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        ${if (projectInfo.kotlinVersion == KotlinVersion.Dev) "maven(\"https://maven.pkg.jetbrains.space/kotlin/p/kotlin/dev/\")" else NAN}
    }
}
    """.trimIndent().deleteNans()
}