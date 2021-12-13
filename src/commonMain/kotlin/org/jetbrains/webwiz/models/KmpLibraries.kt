package org.jetbrains.webwiz.models

import org.jetbrains.webwiz.models.SourceSetType.MAIN

enum class KmpLibrary(
    val targets: Set<Target>?, //null means any target
    val userName: String,
    val dep: String,
    val sourceSetType: SourceSetType
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
        "org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2",
        MAIN
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
        "org.jetbrains.kotlinx:kotlinx-serialization-json:1.3.1",
        MAIN
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
        "org.jetbrains.kotlinx:kotlinx-datetime:0.3.1",
        MAIN
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
        "co.touchlab:kermit:1.0.0",
        MAIN
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
        "io.github.aakira:napier:2.1.0",
        MAIN
    ),
    SQLDELIGHT_COROUTINES(
        setOf(
            Target.ANDROID,
            Target.JVM,
            Target.JS,
            Target.IOS,
            Target.LINUX,
            Target.MACOS,
            Target.WINDOWS,
            Target.TV_OS,
            Target.WATCH_OS
        ),
        "SQLDelight Coroutines",
        "com.squareup.sqldelight:coroutines-extensions:1.5.3",
        MAIN
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
        "io.ktor:ktor-client-core:1.6.7",
        MAIN
    ),
}