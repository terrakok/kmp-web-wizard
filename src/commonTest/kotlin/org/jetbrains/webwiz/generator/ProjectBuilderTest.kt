package org.jetbrains.webwiz.generator

import org.jetbrains.webwiz.generator.files.ModuleBuildGradle
import org.jetbrains.webwiz.generator.files.SettingsGradle
import org.jetbrains.webwiz.models.GradlePlugin
import org.jetbrains.webwiz.models.KmpLibrary
import org.jetbrains.webwiz.models.KotlinVersion
import org.jetbrains.webwiz.models.ProjectInfo
import org.jetbrains.webwiz.models.Target.*
import kotlin.test.Test
import kotlin.test.assertEquals


internal class ProjectBuilderTest {

    @Test
    fun testGeneratedStructure() {
        val projectInfo = ProjectInfo(
            "wizard-test",
            "sdk",
            "my.sdk.package",
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
            sdk/build.gradle.kts
            sdk/src/commonMain/kotlin/my/sdk/package/Platform.kt
            sdk/src/jvmMain/kotlin/my/sdk/package/Platform.kt
            sdk/src/androidMain/kotlin/my/sdk/package/Platform.kt
            sdk/src/jsMain/kotlin/my/sdk/package/Platform.kt
            sdk/src/iosMain/kotlin/my/sdk/package/Platform.kt
            sdk/src/androidMain/AndroidManifest.xml
            sdk/src/commonTest/kotlin/my/sdk/package/CommonTest.kt
            sdk/src/jvmTest/kotlin/my/sdk/package/PlatformTest.kt
            sdk/src/androidTest/kotlin/my/sdk/package/PlatformTest.kt
            sdk/src/jsTest/kotlin/my/sdk/package/PlatformTest.kt
            sdk/src/iosTest/kotlin/my/sdk/package/PlatformTest.kt
        """.trimIndent()
        assertEquals(expect, actual)
    }

    @Test
    fun testGeneratedBuildConfig() {
        val projectInfo = ProjectInfo(
            "wizard-test",
            "sdk",
            "my.sdk.package",
            KotlinVersion.EAP,
            setOf(JVM, JS, IOS, ANDROID),
            setOf(KmpLibrary.SERIALIZATION, KmpLibrary.SQLDELIGHT_DRIVER_NATIVE),
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
            
            /* required for maven publication */
            group = "my.sdk.package"
            version = "0.1"
            
            kotlin {
                jvm()
                android()
                js {
                    browser()
                    nodejs()
                }
                iosX64()
                iosArm64()
                iosSimulatorArm64()
            
                sourceSets {
                    /* Main source sets */
                    val commonMain by getting {
                        dependencies {
                            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2")
                        }
                    }
                    val jvmMain by getting
                    val androidMain by getting
                    val jsMain by getting
                    val iosMain by creating {
                        dependencies {
                            implementation("com.squareup.sqldelight:native-driver:1.5.3")
                        }
                    }
                    val iosX64Main by getting 
                    val iosArm64Main by getting
                    val iosSimulatorArm64Main by getting
            
                    /* Main hierarchy */
                    jvmMain.dependsOn(commonMain)
                    androidMain.dependsOn(commonMain)
                    jsMain.dependsOn(commonMain)
                    iosMain.dependsOn(commonMain)
                    iosX64Main.dependsOn(iosMain)
                    iosArm64Main.dependsOn(iosMain)
                    iosSimulatorArm64Main.dependsOn(iosMain)
            
                    /* Test source sets */
                    val commonTest by getting {
                        dependencies {
                            implementation(kotlin("test"))
                        }
                    }
                    val jvmTest by getting
                    val androidTest by getting
                    val jsTest by getting
                    val iosTest by creating
                    val iosX64Test by getting 
                    val iosArm64Test by getting
                    val iosSimulatorArm64Test by getting
            
                    /* Test hierarchy */
                    jvmTest.dependsOn(commonTest)
                    androidTest.dependsOn(commonTest)
                    jsTest.dependsOn(commonTest)
                    iosTest.dependsOn(commonTest)
                    iosX64Test.dependsOn(iosTest)
                    iosArm64Test.dependsOn(iosTest)
                    iosSimulatorArm64Test.dependsOn(iosTest)
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
            
        """.trimIndent()
        assertEquals(expect, actual)
    }

    @Test
    fun testGeneratedBuildConfigWithCommonNative() {
        val projectInfo = ProjectInfo(
            "wizard-test",
            "sdk",
            "my.sdk.package",
            KotlinVersion.EAP,
            setOf(JVM, IOS, LINUX, MACOS, ANDROID),
            setOf(KmpLibrary.KERMIT_LOGGER, KmpLibrary.COROUTINES, KmpLibrary.KTOR_CLIENT_IOS, KmpLibrary.SQLDELIGHT_DRIVER_NATIVE),
            setOf(GradlePlugin.PUBLISH, GradlePlugin.APPLICATION),
            true
        )
        val actual = projectInfo.generate().first { it is ModuleBuildGradle }.content
        val expect = """
            plugins {
                kotlin("multiplatform")
                id("com.android.library")
                `maven-publish`
            }
            
            /* required for maven publication */
            group = "my.sdk.package"
            version = "0.1"
            
            kotlin {
                jvm()
                android()
                iosX64()
                iosArm64()
                iosSimulatorArm64()
                linuxX64()
                macosX64()
                macosArm64()
            
                sourceSets {
                    /* Main source sets */
                    val commonMain by getting {
                        dependencies {
                            implementation("co.touchlab:kermit:1.0.0")
                            implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0")
                        }
                    }
                    val nativeMain by creating {
                        dependencies {
                            implementation("com.squareup.sqldelight:native-driver:1.5.3")
                        }
                    }
                    val jvmMain by getting
                    val androidMain by getting
                    val iosMain by creating {
                        dependencies {
                            implementation("io.ktor:ktor-client-ios:2.0.0-beta-1")
                        }
                    }
                    val linuxMain by creating
                    val macosMain by creating
                    val iosX64Main by getting 
                    val iosArm64Main by getting
                    val iosSimulatorArm64Main by getting
                    val linuxX64Main by getting
                    val macosX64Main by getting 
                    val macosArm64Main by getting
            
                    /* Main hierarchy */
                    nativeMain.dependsOn(commonMain)
                    jvmMain.dependsOn(commonMain)
                    androidMain.dependsOn(commonMain)
                    iosMain.dependsOn(nativeMain)
                    iosX64Main.dependsOn(iosMain)
                    iosArm64Main.dependsOn(iosMain)
                    iosSimulatorArm64Main.dependsOn(iosMain)
                    linuxMain.dependsOn(nativeMain)
                    linuxX64Main.dependsOn(linuxMain)
                    macosMain.dependsOn(nativeMain)
                    macosX64Main.dependsOn(macosMain)
                    macosArm64Main.dependsOn(macosMain)
            
                    /* Test source sets */
                    val commonTest by getting {
                        dependencies {
                            implementation(kotlin("test"))
                        }
                    }
                    val nativeTest by creating
                    val jvmTest by getting
                    val androidTest by getting
                    val iosTest by creating
                    val linuxTest by creating
                    val macosTest by creating
                    val iosX64Test by getting 
                    val iosArm64Test by getting
                    val iosSimulatorArm64Test by getting
                    val linuxX64Test by getting
                    val macosX64Test by getting 
                    val macosArm64Test by getting
            
                    /* Test hierarchy */
                    nativeTest.dependsOn(commonTest)
                    jvmTest.dependsOn(commonTest)
                    androidTest.dependsOn(commonTest)
                    iosTest.dependsOn(nativeTest)
                    iosX64Test.dependsOn(iosTest)
                    iosArm64Test.dependsOn(iosTest)
                    iosSimulatorArm64Test.dependsOn(iosTest)
                    linuxTest.dependsOn(nativeTest)
                    linuxX64Test.dependsOn(linuxTest)
                    macosTest.dependsOn(nativeTest)
                    macosX64Test.dependsOn(macosTest)
                    macosArm64Test.dependsOn(macosTest)
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
            
        """.trimIndent()
        assertEquals(expect, actual)
    }

    @Test
    fun testGeneratedSettingsGradle() {
        val projectInfo = ProjectInfo(
            "New Project",
            "module",
            "my.sdk.package",
            KotlinVersion.EAP,
            setOf(JVM, JS, IOS, ANDROID),
            setOf(KmpLibrary.SERIALIZATION),
            setOf(GradlePlugin.PUBLISH),
            true
        )
        val actual = projectInfo.generate().first { it is SettingsGradle }.content
        val expect = """
            pluginManagement {
                repositories {
                    google()
                    gradlePluginPortal()
                    mavenCentral()
                }
            }

            rootProject.name = "New_Project"
            include(":module")
        """.trimIndent()
        assertEquals(expect, actual)
    }

}