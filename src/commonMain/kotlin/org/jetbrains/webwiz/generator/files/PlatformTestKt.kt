package org.jetbrains.webwiz.generator.files

import org.jetbrains.webwiz.generator.ProjectFile
import org.jetbrains.webwiz.models.Target
import org.jetbrains.webwiz.models.ProjectInfo
import org.jetbrains.webwiz.models.isJvm

class CommonPlatformTestKt(val moduleName: String, val projectInfo: ProjectInfo) : ProjectFile {
    override val path = "$moduleName/src/commonTest/kotlin" +
        "/${projectInfo.packageName.replace('.', '/')}" +
        "/CommonTest.kt"
    override val content: String
        get() = """
package ${projectInfo.packageName}

import kotlin.test.Test
import kotlin.test.assertTrue

class CommonTest {

    @Test
    fun testExample() {
        assertTrue(Greeting().greeting().contains("Hello"), "Check 'Hello' is mentioned")
    }
}
""".trimIndent()
}

class TargetPlatformTestKt(val moduleName: String, val target: Target, val projectInfo: ProjectInfo) :
    ProjectFile {
    override val path = "$moduleName/src/${target.targetName}Test/kotlin" +
        "/${projectInfo.packageName.replace('.', '/')}" +
        "/PlatformTest.kt"
    override val content: String = if (target.isJvm()) {
        """
package ${projectInfo.packageName}

import org.junit.Assert.assertTrue
import org.junit.Test

class PlatformTest {

    @Test
    fun testExample() {
        assertTrue("Check ${target.targetName} is mentioned", Greeting().greeting().contains("${target.targetName}"))
    }
}
""".trimIndent()
    } else {
        """
package ${projectInfo.packageName}

import kotlin.test.Test
import kotlin.test.assertTrue

class PlatformTest {

    @Test
    fun testExample() {
        assertTrue(Greeting().greeting().contains("${target.targetName}"), "Check ${target.targetName} is mentioned")
    }
}
""".trimIndent()
    }
}

class IntermediatePlatformTestKt(
    val moduleName: String,
    val intermediateName: String,
    val projectInfo: ProjectInfo
) : ProjectFile {
    private val interName = intermediateName.lowercase().replaceFirstChar { it.uppercaseChar() }

    override val path = "$moduleName/src/${intermediateName.lowercase()}Test/kotlin" +
        "/${projectInfo.packageName.replace('.', '/')}" +
        "/${interName}Test.kt"
    override val content: String
        get() = """
package ${projectInfo.packageName}

import kotlin.test.Test
import kotlin.test.assertTrue

class ${interName}Test {

    @Test
    fun testExample() {
        assertTrue(Greeting().greeting().contains("Hello"), "Check 'Hello' is mentioned")
    }
}
""".trimIndent()
}