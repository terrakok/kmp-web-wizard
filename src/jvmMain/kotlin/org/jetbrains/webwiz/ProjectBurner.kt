package org.jetbrains.webwiz

import org.apache.commons.compress.archivers.zip.UnixStat
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry
import org.apache.commons.compress.archivers.zip.ZipArchiveOutputStream
import org.jetbrains.webwiz.generator.files.GradleBat
import org.jetbrains.webwiz.generator.files.GradleWrapperJar
import org.jetbrains.webwiz.generator.files.Gradlew
import org.jetbrains.webwiz.generator.generate
import org.jetbrains.webwiz.models.ProjectInfo
import java.io.File

fun ProjectInfo.asDir(workingDir: File): File {
    val dir = workingDir.resolve(projectName)
    dir.deleteRecursively()
    dir.mkdirs()

    generate().forEach { projectFile ->
        val f = dir.resolve(projectFile.path)
        f.parentFile.mkdirs()
        f.createNewFile()

        if (projectFile is GradleWrapperJar) {
            f.outputStream().use { out ->
                javaClass.getResourceAsStream("/binaries/gradle-wrapper").use { it.copyTo(out) }
            }
        } else {
            if (projectFile is Gradlew) f.setExecutable(true)
            if (projectFile is GradleBat) f.setExecutable(true)

            f.writeText(projectFile.content)
        }
    }
    return dir
}

fun ProjectInfo.asZip(workingDir: File) =
    asDir(workingDir).zip().also { it.deleteRecursively() }

fun File.zip(): File {
    val dir = this
    val resultZip = dir.parentFile.resolve("${dir.name}.zip")
    ZipArchiveOutputStream(resultZip).use { output ->
        dir.walk().forEach { file ->
            val entry = ZipArchiveEntry(file.relativeTo(dir).path)

            //Set unix mode to preserve execution rights for gradlew script
            if (file.isDirectory) {
                entry.unixMode = UnixStat.DIR_FLAG or UnixStat.DEFAULT_DIR_PERM
            } else {
                entry.unixMode = UnixStat.FILE_FLAG or
                    if (file.name == "gradlew") UnixStat.DEFAULT_DIR_PERM else UnixStat.DEFAULT_FILE_PERM
            }

            output.putArchiveEntry(entry)
            if (!file.isDirectory) {
                file.inputStream().use { it.copyTo(output) }
            }
            output.closeArchiveEntry()
        }
    }

    return resultZip
}