rootProject.name = "spekt"

include("api-client-codegen")
include("common")
include("openapi-3-0")
include("playground")
include("swagger-2-0")

// Set Project Gradle Names
run {
  rootProject.children.forEach { it.name = "${rootProject.name}-${it.name}" }
}

// Feature Previews
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

// Plugin Repositories
pluginManagement {
  repositories {
    gradlePluginPortal()
    mavenLocal()
  }
}
