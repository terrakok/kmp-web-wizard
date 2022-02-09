package org.jetbrains.webwiz.models

import org.jetbrains.webwiz.models.SourceSetType.MAIN
import org.jetbrains.webwiz.models.Target.*

private const val KtorVersion = "2.0.0-beta-1"
private const val SqlDelightVersion = "1.5.3"
private const val RealmVersion = "0.9.0"

enum class KmpLibrary(
    val targets: Set<Target>?, //null means any target
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
    SQLDELIGHT_COROUTINES(
        setOf(ANDROID, JVM, JS, IOS, LINUX, MACOS, WINDOWS, TV_OS, WATCH_OS),
        "SQLDelight Coroutines",
        "com.squareup.sqldelight:coroutines-extensions:$SqlDelightVersion",
        MAIN
    ),
    KTOR_CORE(
        setOf(JVM, ANDROID, JS, IOS, LINUX, MACOS, WINDOWS),
        "Ktor Core",
        "io.ktor:ktor-client-core:$KtorVersion",
        MAIN
    ),
    REALM_KOTLIN(
        setOf(JVM, ANDROID, IOS, MACOS),
        "Realm Kotlin Multiplatform",
        "io.realm.kotlin:library-base:$RealmVersion",
        MAIN
    ),
}

enum class SingleTargetLibrary(
    val target: Target,
    val userName: String,
    val dep: String,
    val sourceSetType: SourceSetType
) {
    KTOR_CLIENT_IOS(
        IOS,
        "Ktor iOS Client",
        "io.ktor:ktor-client-ios:$KtorVersion",
        MAIN
    ),
    KTOR_CLIENT_ANDROID(
        ANDROID,
        "Ktor Android Client",
        "io.ktor:ktor-client-okhttp:$KtorVersion",
        MAIN
    ),
    KTOR_CLIENT_JVM(
        JVM,
        "Ktor JVM Client",
        "io.ktor:ktor-client-jvm:$KtorVersion",
        MAIN
    ),
    KTOR_CLIENT_JS(
        JS,
        "Ktor JS Client",
        "io.ktor:ktor-client-js:$KtorVersion",
        MAIN
    ),
    SQLDELIGHT_DRIVER_ANDROID(
        ANDROID,
        "SQLDelight Android Driver",
        "com.squareup.sqldelight:android-driver:$SqlDelightVersion",
        MAIN
    ),
    SQLDELIGHT_DRIVER_JVM(
        JVM,
        "SQLDelight JVM Driver",
        "com.squareup.sqldelight:sqlite-driver:$SqlDelightVersion",
        MAIN
    ),
    SQLDELIGHT_DRIVER_JS(
        JS,
        "SQDelight JS Driver",
        "com.squareup.sqldelight:sqljs-driver:$SqlDelightVersion",
        MAIN
    ),
}

// Dependencies in here will be available for all native targets
enum class NativeTargetLibrary(
    val userName: String,
    val dep: String,
    val sourceSetType: SourceSetType
) {
    SQLDELIGHT_DRIVER_NATIVE(
        "SQLDelight Native Driver",
        "com.squareup.sqldelight:native-driver:$SqlDelightVersion",
        MAIN
    ),
}