package org.jetbrains.webwiz.models

enum class Target(val targetName: String, val userName: String) {
    JVM("jvm", "JVM"),
    ANDROID("android", "Android"),
    JS("js", "JS"),
    IOS("ios", "iOS"),
    TV_OS("tvos", "tvOS"),
    WATCH_OS("watchos", "watchOS"),
    LINUX("linux", "Linux"),
    MACOS("macos", "macOS"),
    WINDOWS("windows", "Windows"),
    WASM("wasm", "WASM"),
    ANDROID_NATIVE("androidNative", "Android Native")
}

private val NativeTargets = setOf(
    Target.WASM,
    Target.ANDROID_NATIVE,
    Target.LINUX,
    Target.MACOS,
    Target.IOS,
    Target.TV_OS,
    Target.WATCH_OS,
    Target.WINDOWS
)

private val CommonNativeTargets = setOf(
    Target.LINUX,
    Target.MACOS,
    Target.IOS,
    Target.TV_OS,
    Target.WATCH_OS,
    Target.WINDOWS
)

fun Target.isJvm() = this in setOf(Target.JVM, Target.ANDROID)

fun Set<Target>.isNativeTargetPresent() = this.any { it in NativeTargets }
fun Set<Target>.isCommonNativeTargetPresent() = this.any { it in CommonNativeTargets }
fun Set<Target>.isAndroidTargetPresent() = this.any { it == Target.ANDROID }
fun Set<Target>.isJvmTargetPresent() = this.any { it.isJvm() }
