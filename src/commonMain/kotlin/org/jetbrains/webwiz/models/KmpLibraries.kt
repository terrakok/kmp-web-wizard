package org.jetbrains.webwiz.models

import org.jetbrains.webwiz.models.SourceSetType.MAIN
import org.jetbrains.webwiz.models.Target.*

private const val KtorVersion = "2.0.0-beta-1"
private const val SqlDelightVersion = "1.5.3"
private const val RealmVersion = "0.9.0"

enum class KmpLibrary(
    val targets: Set<Target>,
    val userName: String,
    val dep: String,
    val sourceSetType: SourceSetType
) {
    COROUTINES(
        setOf(ANDROID, JVM, JS, MACOS, IOS, TV_OS, WATCH_OS, LINUX, WINDOWS),
        "KotlinX Coroutines",
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0",
        MAIN
    ),
    SERIALIZATION(
        setOf(ANDROID, JVM, JS, MACOS, IOS, TV_OS, WATCH_OS, LINUX, WINDOWS),
        "KotlinX Serialization",
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.2",
        MAIN
    ),
    DATE_TIME(
        setOf(ANDROID, JVM, JS, MACOS, IOS, TV_OS, WATCH_OS, LINUX, WINDOWS),
        "KotlinX DateTime",
        "org.jetbrains.kotlinx:kotlinx-datetime:0.3.1",
        MAIN
    ),
    KERMIT_LOGGER(
        setOf(ANDROID, JVM, JS, MACOS, IOS, TV_OS, WATCH_OS, LINUX, WINDOWS),
        "Kermit Logger",
        "co.touchlab:kermit:1.0.0",
        MAIN
    ),
    NAPIER_LOGGER(
        setOf(ANDROID, JVM, JS, MACOS, IOS, TV_OS, WATCH_OS),
        "Napier logger",
        "io.github.aakira:napier:2.3.0",
        MAIN
    ),
    REALM_KOTLIN(
        setOf(JVM, ANDROID, IOS, MACOS),
        "Realm Kotlin Multiplatform",
        "io.realm.kotlin:library-base:$RealmVersion",
        MAIN
    ),
    KTOR_CORE(
        setOf(JVM, ANDROID, JS, IOS, LINUX, MACOS, WINDOWS),
        "Ktor Core",
        "io.ktor:ktor-client-core:$KtorVersion",
        MAIN
    ),
    KTOR_CLIENT_IOS(
        setOf(IOS),
        "Ktor iOS Client",
        "io.ktor:ktor-client-ios:$KtorVersion",
        MAIN
    ),
    KTOR_CLIENT_ANDROID(
        setOf(ANDROID),
        "Ktor Android Client",
        "io.ktor:ktor-client-okhttp:$KtorVersion",
        MAIN
    ),
    KTOR_CLIENT_JVM(
        setOf(JVM),
        "Ktor JVM Client",
        "io.ktor:ktor-client-jvm:$KtorVersion",
        MAIN
    ),
    KTOR_CLIENT_JS(
        setOf(JS),
        "Ktor JS Client",
        "io.ktor:ktor-client-js:$KtorVersion",
        MAIN
    ),
    SQLDELIGHT_COROUTINES(
        setOf(ANDROID, JVM, JS, IOS, LINUX, MACOS, WINDOWS, TV_OS, WATCH_OS),
        "SQLDelight Coroutines",
        "com.squareup.sqldelight:coroutines-extensions:$SqlDelightVersion",
        MAIN
    ),
    SQLDELIGHT_DRIVER_ANDROID(
        setOf(ANDROID),
        "SQLDelight Android Driver",
        "com.squareup.sqldelight:android-driver:$SqlDelightVersion",
        MAIN
    ),
    SQLDELIGHT_DRIVER_JVM(
        setOf(JVM),
        "SQLDelight JVM Driver",
        "com.squareup.sqldelight:sqlite-driver:$SqlDelightVersion",
        MAIN
    ),
    SQLDELIGHT_DRIVER_JS(
        setOf(JS),
        "SQDelight JS Driver",
        "com.squareup.sqldelight:sqljs-driver:$SqlDelightVersion",
        MAIN
    ),
    SQLDELIGHT_DRIVER_NATIVE(
        setOf(MACOS, IOS, TV_OS, WATCH_OS, LINUX, WINDOWS),
        "SQLDelight Native Driver",
        "com.squareup.sqldelight:native-driver:$SqlDelightVersion",
        MAIN
    );

    fun canBeApplied(targets: Set<Target>): Boolean {
        val isCommon = targets.size == Target.values().size
        if (isCommon) return true

        val isSingleTargetLib = this.targets.size == 1
        if (isSingleTargetLib) {
            return targets.contains(this.targets.single())
        }

        val isNativeTargetLib = this.targets.isNativeTargets()
        if (isNativeTargetLib) {
            return targets.any { it.isNative() }
        }

        //corner case:
        //Kotlin MPP supports ios+android library in ios+android+js projects
        //but it requires unusual source sets hierarchy which is not supported by this wizard

        return targets.all { it in this.targets }
    }
}