package org.jetbrains.webwiz.generator.files

import org.jetbrains.webwiz.generator.ProjectFile
import org.jetbrains.webwiz.models.ProjectInfo

class AndroidManifestXml(val projectInfo: ProjectInfo) : ProjectFile {
    override val path = "${projectInfo.moduleName}/src/androidMain/AndroidManifest.xml"
    override val content: String
        get() = """
<?xml version="1.0" encoding="utf-8"?>
<manifest package="${projectInfo.packageName}" />
""".trimIndent()
}