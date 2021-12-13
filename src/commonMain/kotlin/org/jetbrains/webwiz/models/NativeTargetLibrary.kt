package org.jetbrains.webwiz.models

import org.jetbrains.webwiz.models.SourceSetType.MAIN

// Dependencies in here will be available for all native targets
enum class NativeTargetLibrary(
    val userName: String,
    val dep: String,
    val sourceSetType: SourceSetType
) {
    SQLDELIGHT_DRIVER_NATIVE(
        "SQDelight Native Driver",
        "com.squareup.sqldelight:native-driver:1.5.3",
        MAIN
    ),
}