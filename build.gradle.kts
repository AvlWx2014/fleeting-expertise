@file:OptIn(ExperimentalComposeLibrary::class)

import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.compose.ExperimentalComposeLibrary

plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
}

group = "dev.avlwx"
version = "1.0-SNAPSHOT"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    jvm {
        jvmToolchain(11)
        withJava()
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
                implementation(kotlin("reflect"))

                implementation("com.fasterxml.jackson:jackson:2.13.4")
                implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.4")

                implementation ("com.squareup.retrofit2:retrofit:2.9.0")

                implementation("com.squareup.okhttp3:okhttp:4.10.0")
                implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
                implementation("com.squareup.okhttp3:okhttp-tls:4.10.0")

                languageSettings.optIn("androidx.compose.material3.ExperimentalMaterial3Api")
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "Main"
        nativeDistributions {
            targetFormats(TargetFormat.Rpm)
            packageName = "koji-buildroot-resolver"
            packageVersion = "1.0.0"
        }
    }
}
