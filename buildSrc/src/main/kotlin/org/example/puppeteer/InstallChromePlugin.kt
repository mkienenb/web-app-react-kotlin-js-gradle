package org.example.puppeteer

import java.io.File
import java.util.concurrent.TimeUnit
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.GradleException
import org.gradle.internal.os.OperatingSystem

class InstallChromePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        with(project) {
            val chromePathFile = project.layout.buildDirectory.file("puppeteer/chrome-path.txt").get().asFile

            tasks.register("installPuppeteerChromium") {
                dependsOn("kotlinNodeJsSetup", "kotlinNpmInstall")

                // Declare marker file as task output
                outputs.file(chromePathFile)

                // Skip the task entirely if Chrome is already installed (based on saved path)
                outputs.upToDateWhen {
                    if (!chromePathFile.exists()) return@upToDateWhen false
                    val savedPath = chromePathFile.readText().trim()
                    val chromeBin = File(savedPath)
                    chromeBin.exists() && chromeBin.canExecute()
                }

                doLast {
                    if (chromePathFile.exists()) {
                        val savedPath = chromePathFile.readText().trim()
                        val chromeBin = File(savedPath)
                        if (chromeBin.exists() && chromeBin.canExecute()) {
                            println("✅ Chrome already installed at: $savedPath — skipping install.")
                            project.extensions.extraProperties["org.example.puppeteerChromePath"] = savedPath
                            return@doLast
                        }
                    }

                    val chromeExecutable = when {
                        OperatingSystem.current().isWindows -> "chrome-win64/chrome.exe"
                        OperatingSystem.current().isMacOsX -> "chrome-mac-arm64/Chromium.app/Contents/MacOS/Chromium"
                        else -> "chrome-linux64/chrome"
                    }

                    val puppeteerDir = file("build/js/node_modules/puppeteer")
                    val installScript = File(puppeteerDir, "install.mjs")
                    if (!installScript.exists()) {
                        throw GradleException("Puppeteer install.mjs not found at: $installScript")
                    }

                    val nodeExecutable = File(System.getProperty("user.home"))
                        .resolve(".gradle/nodejs")
                        .walk()
                        .firstOrNull { it.name in listOf("node", "node.exe") && it.canExecute() && it.isFile() }
                        ?: throw GradleException("Managed Node.js not found in ~/.gradle/nodejs")

                    val command = listOf(nodeExecutable.absolutePath, installScript.absolutePath)
                    println("Running Puppeteer install with: ${command.joinToString(" ")}")

                    val processBuilder = ProcessBuilder(command)
                        .directory(puppeteerDir)
                        .redirectErrorStream(true)
                        .apply {
                            environment()["CI"] = "true"
                            environment()["PUPPETEER_NO_PROGRESS"] = "true"
                            environment()["PUPPETEER_SKIP_POSTINSTALL"] = "true"
                            environment()["PUPPETEER_PRODUCT"] = "chrome"
                            environment()["PUPPETEER_CACHE_DIR"] = File(System.getProperty("user.home"))
                                .resolve(".cache/puppeteer").absolutePath
                        }

                    val process = processBuilder.start()
                    val reader = process.inputStream.bufferedReader()

                    var chromePath: String? = null

                    while (true) {
                        val line = reader.readLine() ?: break
                        println(line)
                        if (line.contains("Chrome") && line.contains("downloaded to")) {
                            val basePath = line.substringAfter("downloaded to").trim()
                            val chromeExecutablePath = File(basePath, chromeExecutable).absolutePath
                            chromePath = chromeExecutablePath

                            println("✅ Chrome downloaded to: $chromePath")
                            chromePathFile.parentFile.mkdirs()
                            chromePathFile.writeText(chromePath)

                            project.extensions.extraProperties["org.example.puppeteerChromePath"] = chromePath

                            if (!OperatingSystem.current().isWindows) {
                                process.destroy()
                            }
                            break
                        }
                    }

                    val finished = process.waitFor(1, TimeUnit.MINUTES)
                    if (!finished) {
                        process.destroyForcibly()
                        throw GradleException("Puppeteer install process hung and was forcibly terminated")
                    }

                    val exit = process.exitValue()
                    if (exit != 0 && !(exit == 143 && chromePath != null)) {
                        throw GradleException("Puppeteer install failed with exit code $exit")
                    }
                }
            }

            // ✅ Clean task: remove marker to force reinstall on next run
            tasks.register("cleanPuppeteerChrome") {
                group = "puppeteer"
                description = "Removes the Puppeteer Chrome path marker to force re-download on next build."

                doLast {
                    if (chromePathFile.exists()) {
                        chromePathFile.delete()
                        println("🧹 Deleted Puppeteer Chrome marker file.")
                    } else {
                        println("ℹ️ No marker file to delete.")
                    }
                }
            }
        }
    }
}

fun resolvePuppeteerChromePath(project: Project): String {
    val chromePathFile = project.layout.buildDirectory.file("puppeteer/chrome-path.txt").get().asFile

    if (!chromePathFile.exists()) {
        throw GradleException("Cannot resolve CHROME_BIN — chrome-path.txt does not exist")
    }

    val chromePath = chromePathFile.readText().trim()
    val chromeBin = File(chromePath)

    if (!chromeBin.exists() || !chromeBin.canExecute()) {
        throw GradleException("CHROME_BIN path is invalid or not executable: $chromePath")
    }

    return chromePath
}
