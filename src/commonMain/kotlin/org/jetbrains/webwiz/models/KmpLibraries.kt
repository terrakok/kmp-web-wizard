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
    LOGGER(
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
    )
}