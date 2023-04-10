import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
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
    }
    sourceSets {
        val jvmMain by getting {
            dependencies {
                // Kotlin Reflect
                implementation(kotlin("reflect"))

                // Compose
                implementation(compose.desktop.currentOs)
                implementation(compose.material3)
                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${extra["coroutines.version"]}")
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-swing:${extra["coroutines.version"]}")

                // Kotlinx Serialization
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:${extra["serialization.core.version"]}")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:${extra["serialization.core.version"]}")
                implementation("io.github.pdvrieze.xmlutil:serialization-jvm:${extra["serialization.xml.version"]}")

                // Retrofit
                implementation ("com.squareup.retrofit2:retrofit:2.9.0")
                implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:0.8.0")

                // OkHttp
                implementation("com.squareup.okhttp3:okhttp:4.10.0")
                implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
                implementation("com.squareup.okhttp3:okhttp-tls:4.10.0")

                // Kotlinx Datetime
                implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.4.0")
            }

            languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
            languageSettings.optIn("kotlin.contracts.ExperimentalContracts")
        }
        val jvmTest by getting {
            dependencies {
                implementation(kotlin("test"))
                implementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
            }
        }
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
