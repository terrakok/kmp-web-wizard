import org.jetbrains.compose.compose
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension

plugins {
    kotlin("multiplatform") version "1.6.10"
    id("org.jetbrains.compose") version "1.0.1"
}

repositories {
    mavenCentral()
    google()
}

kotlin {
    jvm() //for common tests
    js(IR) {
        browser {
            testTask {
                enabled = false
            }
        }
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(compose.runtime)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation("org.apache.commons:commons-compress:1.2")
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.web.widgets)
                implementation(npm("highlight.js", "10.7.2"))
                implementation(npm("file-saver", "2.0.5"))
                implementation(npm("jszip", "3.2.0"))
                implementation(npm("stream", "0.0.2"))
            }
        }
    }
}

task<Copy>("fixMissingResources") {
    dependsOn("jvmProcessResources")
    tasks.findByPath("jvmTest")?.dependsOn(this)

    from("$buildDir/processedResources/jvm/main")
    into("$buildDir/resources/")
}
