package org.jetbrains.webwiz.models

data class ProjectInfo(
    val projectName: String,
    val moduleName: String,
    val packageName: String,
    val kotlinVersion: KotlinVersion,
    val targets: Set<Target>,
    val dependencies: Set<KmpLibrary>,
    val singleTargetDependencies: Set<SingleTargetLibrary>,
    val commonNativeTargetLibraries: Set<CommonNativeTargetLibrary>,
    val gradlePlugins: Set<GradlePlugin>,
    val enableTests: Boolean
) {
    fun validate() {
        require(!moduleName.contains(' ')) {
            "module name contains space character"
        }
        require(gradlePlugins.all { it.canBeApplied(targets) }) {
            "incorrect gradle plugin was used for current set of targets"
        }
        require(dependencies.all { dep -> dep.targets == null || targets.all { dep.targets.contains(it) } }) {
            "incorrect dependency was used for current set of targets"
        }
        require(singleTargetDependencies.all { dep -> dep.target in targets}) {
            "incorrect dependency was used for current set of targets"
        }
    }

    fun normalize() = copy(
        targets = targets.sortedBy { it.ordinal }.toSet(),
        moduleName = moduleName.replace(' ', '_'),
        gradlePlugins = gradlePlugins.filter { it.canBeApplied(targets) }.toSet(),
        dependencies = dependencies.filter { dep -> dep.targets == null || targets.all { dep.targets.contains(it) } }.toSet(),
        singleTargetDependencies = singleTargetDependencies.filter { dep -> dep.target in targets }.toSet(),
        commonNativeTargetLibraries = if (targets.isCommonNativeTargetPresent()) commonNativeTargetLibraries else emptySet()
    )
}
