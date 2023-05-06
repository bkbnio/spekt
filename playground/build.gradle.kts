import org.gradle.kotlin.dsl.main
import org.gradle.kotlin.dsl.sourceSets

plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("io.bkbn.sourdough.application.jvm")
  id("org.jetbrains.kotlinx.kover")
  id("application")
}

sourdoughApp {
  compilerArgs.set(listOf("-opt-in=kotlin.RequiresOptIn"))
}

kotlin {
  sourceSets.main {
    kotlin.srcDir("src/gen/kotlin")
  }
}

dependencies {
  // IMPLEMENTATION
  implementation(projects.spektOpenapi30)
  implementation(projects.spektApiClientCodegen)
  implementation("io.ktor:ktor-client-core:2.3.0")
  implementation("io.ktor:ktor-client-cio:2.3.0")
  implementation("io.ktor:ktor-client-content-negotiation:2.3.0")
  implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.0")
  implementation("io.ktor:ktor-client-auth:2.3.0")
}
