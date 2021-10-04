package org.jetbrains.webwiz.generator.files

import org.jetbrains.webwiz.generator.ProjectFile

class Gitignore : ProjectFile {
    override val path = ".gitignore"
    override val content: String
        get() = """
*.iml
.gradle
/local.properties
.idea
.DS_Store
/build
*/build
/captures
.externalNativeBuild
.cxx
local.properties
    """.trimIndent()
}