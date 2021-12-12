package org.jetbrains.webwiz.models

enum class KmpLibrary(
    val targets: Set<Target>?, //null means any target
    val userName: String,
    val dep: String
) {
    COROUTINES(
        setOf(
            Target.ANDROID,
            Target.JVM,
            Target.JS,
            Target.MACOS,
            Target.IOS,
            Target.TV_OS,
            Target.WATCH_OS,
            Target.LINUX,
            Target.WINDOWS
        ),
        "KotlinX Coroutines",
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2"
    ),
    SERIALIZATION(
        setOf(
            Target.ANDROID,
            Target.JVM,
            Target.JS,
            Target.MACOS,
            Target.IOS,
            Target.TV_OS,
            Target.WATCH_OS,
            Target.LINUX,
            Target.WINDOWS
        ),
        "KotlinX Serialization",
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1"
    ),
    DATE_TIME(
        setOf(
            Target.ANDROID,
            Target.JVM,
            Target.JS,
            Target.MACOS,
            Target.IOS,
            Target.TV_OS,
            Target.WATCH_OS,
            Target.LINUX,
            Target.WINDOWS
        ),
        "KotlinX DateTime",
        "org.jetbrains.kotlinx:kotlinx-datetime:0.3.1"
    ),
    KERMIT_LOGGER(
        setOf(
            Target.ANDROID,
            Target.JVM,
            Target.JS,
            Target.MACOS,
            Target.IOS,
            Target.TV_OS,
            Target.WATCH_OS,
            Target.LINUX,
            Target.WINDOWS
        ),
        "Kermit Logger",
        "co.touchlab:kermit:1.0.0"
    ),
    NAPIER_LOGGER(
        setOf(
            Target.ANDROID,
            Target.JVM,
            Target.JS,
            Target.MACOS,
            Target.IOS,
            Target.TV_OS,
            Target.WATCH_OS
        ),
        "Napier logger",
        "io.github.aakira:napier:2.1.0"
    ),
    SQLDELIGHT_COROUTINES(
        setOf(
            Target.ANDROID,
            Target.JVM,
            Target.JS,
            Target.IOS,
            Target.MACOS,
            Target.WINDOWS
        ),
        "SQLDelight Coroutines",
        "com.squareup.sqldelight:coroutines-extensions:1.5.3"
    ),
    KTOR_CORE(
        setOf(
            Target.JVM,
            Target.ANDROID,
            Target.JS,
            Target.IOS,
            Target.LINUX,
            Target.MACOS,
            Target.WINDOWS
        ),
        "Ktor Core",
        "io.ktor:ktor-client-core:1.6.7"
    ),
}