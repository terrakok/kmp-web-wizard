package org.jetbrains.webwiz.generator.files

import org.jetbrains.webwiz.generator.ProjectFile
import org.jetbrains.webwiz.models.ProjectInfo
import org.jetbrains.webwiz.models.Target

class CommonPlatformKt(val projectInfo: ProjectInfo) : ProjectFile {
    override val path = "${projectInfo.moduleName}/src/commonMain/kotlin" +
            "/${projectInfo.packageName.replace('.', '/')}" +
            "/Platform.kt"
    override val content: String
        get() = """
package ${projectInfo.packageName}

expect val platform: String

class Greeting {
    fun greeting() = "Hello, ${'$'}platform!"
}
    """.trimIndent()
}

class TargetPlatformKt(val target: Target, val projectInfo: ProjectInfo) : ProjectFile {
    override val path = "${projectInfo.moduleName}/src/${target.targetName}Main/kotlin" +
            "/${projectInfo.packageName.replace('.', '/')}" +
            "/Platform.kt"
    override val content: String
        get() = """
package ${projectInfo.packageName}

actual val platform: String = "${target.targetName}"
    """.trimIndent()
}

class IntermediatePlatformKt(val intermediateName: String, val projectInfo: ProjectInfo) : ProjectFile {
    override val path = "${projectInfo.moduleName}/src/${intermediateName}Main/kotlin" +
            "/${projectInfo.packageName.replace('.', '/')}" +
            "/Platform.kt"
    override val content: String
        get() = """
package ${projectInfo.packageName}

    """.trimIndent()
}