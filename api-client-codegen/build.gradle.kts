plugins {
  kotlin("jvm")
  kotlin("plugin.serialization")
  id("io.bkbn.sourdough.library.jvm")
  id("io.gitlab.arturbosch.detekt")
  id("com.adarshr.test-logger")
  id("org.jetbrains.kotlinx.kover")
  id("maven-publish")
  id("java-library")
}

dependencies {
  // Versions
  val detektVersion: String by project

  // Spekt
  implementation(projects.spektCommon)
  implementation(projects.spektOpenapi30)
  implementation(projects.spektSwagger20)

  api("com.squareup:kotlinpoet:1.13.1")

  // Formatting
  detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:$detektVersion")
}

sourdoughLibrary {
  libraryName.set("Spekt API Client Codegen")
  libraryDescription.set("Generates KotlinPoet Manifests for creating HTTP Clients from a Spekt API Spec")
}

testing {
  suites {
    named<JvmTestSuite>("test") {
      useJUnitJupiter()
      dependencies {
        // Kotest
        implementation("io.kotest:kotest-runner-junit5-jvm:5.5.4")
        implementation("io.kotest:kotest-assertions-core-jvm:5.5.4")
      }
    }
  }
}
