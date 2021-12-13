package org.jetbrains.webwiz.models

import org.jetbrains.webwiz.models.SourceSetType.MAIN

enum class SingleTargetLibrary(
    val target: Target,
    val userName: String,
    val dep: String,
    val sourceSetType: SourceSetType
) {
    KTOR_CLIENT_IOS(
        Target.IOS,
        "Ktor iOS Client",
        "io.ktor:ktor-client-ios:1.6.7",
        MAIN
    ),
    KTOR_CLIENT_OKHTTP(
        Target.ANDROID,
        "Ktor OkHttp Client",
        "io.ktor:ktor-client-okhttp:1.6.7",
        MAIN
    ),
    KTOR_CLIENT_JVM(
        Target.JVM,
        "Ktor JVM Client",
        "io.ktor:ktor-client-jvm:1.6.7",
        MAIN
    ),
    KTOR_CLIENT_JS(
        Target.JS,
        "Ktor JS Client",
        "io.ktor:ktor-client-js:1.6.7",
        MAIN
    ),
    SQLDELIGHT_DRIVER_ANDROID(
        Target.ANDROID,
        "SQLDelight Android Driver",
        "com.squareup.sqldelight:android-driver:1.5.3",
        MAIN
    ),
    SQLDELIGHT_DRIVER_JVM(
        Target.JVM,
        "SQLDelight JVM Driver",
        "com.squareup.sqldelight:sqlite-driver:1.5.3",
        MAIN
    ),
    SQLDELIGHT_DRIVER_JS(
        Target.JS,
        "SQDelight JS Driver",
        "com.squareup.sqldelight:sqljs-driver:1.5.3",
        MAIN
    ),
}
