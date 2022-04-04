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
    WINDOWS("windows", "Windows")
}

private val NativeTargets = setOf(
    Target.LINUX,
    Target.MACOS,
    Target.IOS,
    Target.TV_OS,
    Target.WATCH_OS,
    Target.WINDOWS
)

fun Target.isJvm() = this in setOf(Target.JVM, Target.ANDROID)

fun Set<Target>.isNativeTargetPresent() = this.any { it in NativeTargets }
fun Set<Target>.isCommonNativeTargetPresent() = this.filter { it in NativeTargets }.size > 1
