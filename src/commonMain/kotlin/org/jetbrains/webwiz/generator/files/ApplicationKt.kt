package org.jetbrains.webwiz.generator.files

import org.jetbrains.webwiz.generator.ProjectFile
import org.jetbrains.webwiz.models.ProjectInfo

class ApplicationKt(val moduleName: String, val projectInfo: ProjectInfo) : ProjectFile {
    override val path = "$moduleName/src/jvmMain/kotlin" +
            "/${projectInfo.packageName.replace('.', '/')}" +
            "/Application.kt"
    override val content: String
        get() = """
package ${projectInfo.packageName}

fun main() {
    println("Hello world!")
}
""".trimIndent()
}