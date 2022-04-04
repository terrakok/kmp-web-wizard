package org.jetbrains.webwiz

import org.jetbrains.webwiz.models.GradlePlugin
import org.jetbrains.webwiz.models.KmpLibrary
import org.jetbrains.webwiz.models.KotlinVersion
import org.jetbrains.webwiz.models.ProjectInfo
import org.jetbrains.webwiz.models.Target
import org.junit.AfterClass
import org.junit.BeforeClass
import org.junit.Test
import java.io.File
import java.lang.ProcessBuilder.*
import kotlin.io.path.createTempDirectory
import kotlin.test.assertEquals

class GeneratedProjectTest {

    companion object {
        private lateinit var workingDir: File

        @BeforeClass
        @JvmStatic
        fun prepare() {
            workingDir = createTempDirectory().toFile()
        }

        @AfterClass
        @JvmStatic
        fun release() {
            workingDir.deleteRecursively()
        }
    }

    @Test
    fun testGeneratedProjectJvmLinux() {
        checkProject(
            ProjectInfo(
                "wizard-sample",
                "library",
                "my.sdk.awesome",
                KotlinVersion.Stable,
                setOf(Target.JVM, Target.LINUX),
                setOf(KmpLibrary.KERMIT_LOGGER, KmpLibrary.COROUTINES, KmpLibrary.DATE_TIME, KmpLibrary.SQLDELIGHT_DRIVER_NATIVE),
                setOf(GradlePlugin.PUBLISH),
                true
            )
        )
    }

    private fun checkProject(projectInfo: ProjectInfo) {
        val dir = projectInfo.asDir(workingDir)

        println("Project dir: ${dir.absolutePath}")
        println("============start of the build============")
        val proc = ProcessBuilder("${dir.path}/gradlew", "check", "--info").apply {
            directory(dir)
            redirectOutput(Redirect.INHERIT)
            redirectError(Redirect.INHERIT)
        }.start()

        proc.waitFor()
        println("============end of the build============")
        assertEquals(0, proc.exitValue(), "'./gradlew check --info' exit code")

    }
}