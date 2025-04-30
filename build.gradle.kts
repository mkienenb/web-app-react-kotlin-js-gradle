import org.example.puppeteer.InstallChromePlugin
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.kotest)
    alias(libs.plugins.jspackage)
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

kotlin {
    jvm { }
    js(IR) {
        browser {
            testTask {
                useKarma {
                    useChromeHeadless()
                }
            }
            commonWebpackConfig {
                // Let the dev server pick a random port.
                devServer?.port = 0
                // Configure the browser DSL to disable auto-opening when the org.example.headless property is set.
                devServer?.open = !project.hasProperty("org.example.headless")
                cssSupport {
                    enabled.set(true)
                }
            }
        }
        nodejs {
            testTask {
                useMocha {
                    timeout = "20s"
                }
                testLogging {
                    events("passed", "failed", "skipped")
                    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                    showStandardStreams = true
                }
            }
        }
        compilations.named("test") {
            packageJson { customField("mocha", mapOf("require" to "global-jsdom/register")) }
        }
        binaries.executable()
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutines.core)
                implementation("org.kodein.di:kodein-di:7.25.0")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.kotest.assertions.core)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(project.dependencies.enforcedPlatform(libs.kotlin.wrappers.bom))
                implementation(libs.kotlin.react)
                implementation(libs.kotlin.react.dom)
                implementation(libs.kotlinx.serialization.json)
                implementation("org.kodein.di:kodein-di-js:7.25.0")
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(libs.kotest.framework.engine.js)
                implementation(libs.kotlin.react.dom.test.utils.js)
                implementation(project.dependencies.enforcedPlatform(libs.jsmints.bom))
                implementation(libs.kotlin.react.testing.library)
                implementation(libs.kotlin.user.event.testing.library.js)
                implementation(npm("jsdom", "26.1.0"))
                implementation(npm("global-jsdom", "26.0.0"))

                implementation(npm("puppeteer", libs.versions.puppeteer.get()))
            }
        }
        val jvmTest by getting {
            dependencies {
                implementation(libs.kotest.runner.junit5)

                implementation(project.dependencies.platform("org.junit:junit-bom:${libs.versions.junit.get()}"))
                implementation("org.junit.jupiter:junit-jupiter-api")
                implementation("org.junit.platform:junit-platform-suite")

                implementation(project.dependencies.platform("io.cucumber:cucumber-bom:${libs.versions.cucumber.get()}"))
                implementation(project.dependencies.platform("org.seleniumhq.selenium:selenium-dependencies-bom:${libs.versions.selenium.get()}"))

                implementation(libs.cucumber.java)
                implementation(libs.cucumber.junit.platform.engine)
                implementation(libs.cucumber.picocontainer)

                implementation(libs.kotest.assertions.core)
                implementation(libs.selenium.java)

                implementation(libs.ktor.server.core)
                implementation(libs.ktor.server.netty)
                implementation(libs.ktor.server.cors)
            }
        }
    }
}

dependencies {
    add("kspJvmTest", project(":ksp-processor"))
}

tasks.named<Test>("jvmTest") {
    useJUnitPlatform()

    // Enable full Cucumber debug output.
    systemProperty("cucumber.execution.verbose", "true")

    testLogging {
        showStandardStreams = true
        events("passed", "skipped", "failed", "standard_out", "standard_error")
    }
}

tasks.named("check") {
    dependsOn("jsTest", "jvmTest")
}

apply<InstallChromePlugin>()

tasks.withType<KotlinJsTest>().configureEach {
    if (name == "jsBrowserTest") {
        dependsOn("installPuppeteerChromium")
        doFirst {
            val chromePath = org.example.puppeteer.resolvePuppeteerChromePath(project)
            println("✅ Using Puppeteer Chrome at: $chromePath")
            environment("CHROME_BIN", chromePath)
        }
    }
}
