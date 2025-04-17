import org.example.puppeteer.InstallChromePlugin
import org.jetbrains.kotlin.gradle.targets.js.testing.KotlinJsTest

val kotestVersion = "5.9.1"

plugins {
    kotlin("multiplatform") version "1.9.25"
    kotlin("plugin.serialization") version "1.9.25"
    id("io.kotest.multiplatform") version "5.9.1"
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
                    timeout = "5000"
                }
                testLogging {
                    events("passed", "failed", "skipped")
                    exceptionFormat = org.gradle.api.tasks.testing.logging.TestExceptionFormat.FULL
                    showStandardStreams = true
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        val kotlinWrapperVersion = "733"
        val kotlinxCoroutinesVersion = "1.8.1"
//        val koinVersion = "4.0.4"

        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$kotlinxCoroutinesVersion")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation("io.kotest:kotest-assertions-core:$kotestVersion")
                implementation("io.kotest:kotest-framework-engine:$kotestVersion")
                implementation("io.kotest:kotest-framework-datatest:$kotestVersion")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:$kotlinxCoroutinesVersion")

                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting {
            dependencies {
                implementation(project.dependencies.enforcedPlatform("org.jetbrains.kotlin-wrappers:kotlin-wrappers-bom:1.0.0-pre.$kotlinWrapperVersion"))
//                implementation("io.insert-koin:koin-core-js:$koinVersion")

                //React, React DOM + Wrappers (chapter 3)
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom")
                implementation("org.jetbrains.kotlin-wrappers:kotlin-tanstack-react-query")

                //Kotlin React Emotion (CSS) (chapter 3)
                implementation("org.jetbrains.kotlin-wrappers:kotlin-emotion")

                //Video Player (chapter 7)
                implementation(npm("react-player", "2.12.0"))

                //Share Buttons (chapter 7)
                implementation(npm("react-share", "4.4.1"))

                //Coroutines & serialization (chapter 8)
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
            }
        }
        // (Client-side tests for Kotlin/JS would live in src/jsTest.)
        val jsTest by getting {
            dependencies {
//                implementation("io.insert-koin:koin-test:$koinVersion")
                implementation(kotlin("test"))
                implementation("org.jetbrains.kotlin-wrappers:kotlin-react-dom-test-utils-js:18.2.0-pre.$kotlinWrapperVersion")
                // for chrome support
                implementation(npm("puppeteer", "21.3.8"))
            }
        }
        // Our JVM tests (integration tests) reside in src/jvmTest.
        val jvmTest by getting {
            dependencies {
                implementation("io.kotest:kotest-runner-junit5:$kotestVersion")
                implementation(platform("org.junit:junit-bom:5.12.1"))
                implementation(platform("io.cucumber:cucumber-bom:7.22.0"))
                implementation(platform("org.assertj:assertj-bom:3.27.3"))
                implementation(platform("org.seleniumhq.selenium:selenium-dependencies-bom:4.31.0"))

                implementation("io.cucumber:cucumber-java")
                implementation("io.cucumber:cucumber-junit-platform-engine")
                implementation("io.cucumber:cucumber-picocontainer")

                implementation("org.junit.jupiter:junit-jupiter-api")
                implementation("org.junit.platform:junit-platform-suite")

                implementation("org.assertj:assertj-core")

                // Selenium dependency for browser automation in tests.
                implementation("org.seleniumhq.selenium:selenium-java")
            }
        }
    }
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

// Heroku Deployment (chapter 9)
tasks.register("stage") {
    dependsOn("build")
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
