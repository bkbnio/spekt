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
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
}

sourdoughLibrary {
  githubOrg.set("bkbnio")
  githubRepo.set("contrakts")
  libraryName.set("contrakts-swagger-2-0")
  libraryDescription.set("Swagger 2.0 Contract Definition")
  licenseName.set("MIT License")
  licenseUrl.set("https://mit-license.org")
  developerId.set("unredundant")
  developerName.set("Ryan Brink")
  developerEmail.set("admin@bkbn.io")
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
