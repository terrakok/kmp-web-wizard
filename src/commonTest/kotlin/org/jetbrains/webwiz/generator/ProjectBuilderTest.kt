package org.jetbrains.webwiz.generator

import org.jetbrains.webwiz.generator.files.ModuleBuildGradle
import org.jetbrains.webwiz.models.GradlePlugin
import org.jetbrains.webwiz.models.KmpLibrary
import org.jetbrains.webwiz.models.Target.*
import org.jetbrains.webwiz.models.KotlinVersion
import org.jetbrains.webwiz.models.ProjectInfo
import kotlin.test.Test
import kotlin.test.assertEquals


internal class ProjectBuilderTest {

    @Test
    fun testGeneratedStructure() {
        val projectInfo = ProjectInfo(
            "wizard-sample",
            "my.test.package",
            KotlinVersion.Stable,
            setOf(JVM, JS, IOS, ANDROID),
            emptySet(),
            setOf(GradlePlugin.PUBLISH),
            true
        )
        val actual = projectInfo.generate().joinToString("\n") { it.path }
        val expect = """
            .gitignore
            gradlew
            gradle.bat
            gradle/wrapper/gradle-wrapper.properties
            gradle/wrapper/gradle-wrapper.jar
            build.gradle.kts
            settings.gradle.kts
            gradle.properties
            shared/build.gradle.kts
            shared/src/commonMain/kotlin/my/test/package/Platform.kt
            shared/src/jvmMain/kotlin/my/test/package/Platform.kt
            shared/src/jsMain/kotlin/my/test/package/Platform.kt
            shared/src/iosMain/kotlin/my/test/package/Platform.kt
            shared/src/androidMain/kotlin/my/test/package/Platform.kt
            shared/src/nativeMain/kotlin/my/test/package/Platform.kt
            shared/src/androidMain/AndroidManifest.xml
            shared/src/commonTest/kotlin/my/test/package/CommonTest.kt
            shared/src/jvmTest/kotlin/my/test/package/PlatformTest.kt
            shared/src/jsTest/kotlin/my/test/package/PlatformTest.kt
            shared/src/iosTest/kotlin/my/test/package/PlatformTest.kt
            shared/src/androidTest/kotlin/my/test/package/PlatformTest.kt
            shared/src/nativeTest/kotlin/my/test/package/NativeTest.kt
        """.trimIndent()
        assertEquals(expect, actual)
    }

    @Test
    fun testGeneratedBuildConfig() {
        val projectInfo = ProjectInfo(
            "wizard-sample",
            "my.test.package",
            KotlinVersion.EAP,
            setOf(JVM, JS, IOS, ANDROID),
            setOf(KmpLibrary.SERIALIZATION),
            setOf(GradlePlugin.PUBLISH),
            true
        )
        val actual = projectInfo.generate().first { it is ModuleBuildGradle }.content
        val expect = """
            plugins {
                kotlin("multiplatform")
                kotlin("plugin.serialization")
                id("com.android.library")
                `maven-publish`
            }
            
            kotlin {
                jvm()
                js()
                iosX64()
                iosArm64()
                /* iosSimulatorArm64() sure all ios dependencies support this target */
                android()
            
                sourceSets {
                    /* Main source sets */
                    val commonMain by getting {
                        dependencies {
                            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
                        }
                    }
                    val jvmMain by getting
                    val jsMain by getting
                    val iosX64Main by getting 
                    val iosArm64Main by getting
                    /* val iosSimulatorArm64Main by getting */
                    val androidMain by getting
                    val iosMain by creating
                    val nativeMain by creating
            
                    /* Main hierarchy */
                    jvmMain.dependsOn(commonMain)
                    jsMain.dependsOn(commonMain)
                    iosMain.dependsOn(nativeMain)
                    iosX64Main.dependsOn(iosMain)
                    iosArm64Main.dependsOn(iosMain)
                    /* iosSimulatorArm64Main.dependsOn(iosMain) */
                    androidMain.dependsOn(commonMain)
                    nativeMain.dependsOn(commonMain)
            
                    /* Test source sets */
                    val commonTest by getting {
                        dependencies {
                            implementation(kotlin("test"))
                        }
                    }
                    val jvmTest by getting
                    val jsTest by getting
                    val iosX64Test by getting 
                    val iosArm64Test by getting
                    /* val iosSimulatorArm64Test by getting */
                    val androidTest by getting
                    val iosTest by creating
                    val nativeTest by creating
            
                    /* Test hierarchy */
                    jvmTest.dependsOn(commonTest)
                    jsTest.dependsOn(commonTest)
                    iosTest.dependsOn(nativeTest)
                    iosX64Test.dependsOn(iosTest)
                    iosArm64Test.dependsOn(iosTest)
                    /* iosSimulatorArm64Test.dependsOn(iosTest) */
                    androidTest.dependsOn(commonTest)
                    nativeTest.dependsOn(commonTest)
                }
            }
            
            android {
                compileSdk = 31
                sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
                defaultConfig {
                    minSdk = 21
                    targetSdk = 31
                }
            }
            
            publishing {
                publications {
                    create<MavenPublication>("maven") {
                        groupId = "my.test.package"
                        artifactId = "wizard-sample"
                        version = "0.1"
                    }
                }
            }
            
        """.trimIndent()
        assertEquals(expect, actual)
    }

}