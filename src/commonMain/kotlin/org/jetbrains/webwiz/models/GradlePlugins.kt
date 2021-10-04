package org.jetbrains.webwiz.models

enum class GradlePlugin(
    val mandatory: Set<Target>,
    val forbidden: Set<Target>,
    val userName: String
) {
    PUBLISH(emptySet(), emptySet(), "Maven Publish"),
    APPLICATION(setOf(Target.JVM), setOf(Target.ANDROID), "JVM Application");

    fun canBeApplied(targets: Set<Target>): Boolean =
        targets.containsAll(mandatory) && !targets.any { it in forbidden }
}