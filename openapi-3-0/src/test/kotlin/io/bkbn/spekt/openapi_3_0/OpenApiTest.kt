package io.bkbn.spekt.openapi_3_0

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import kotlinx.serialization.json.Json

class OpenApiTest : DescribeSpec({
  describe("File Utils") {
    it("Can deserialize the github spec") {
      // arrange
      val spec = readFile("github.json")

      // act
      val result = json.decodeFromString(OpenApi.serializer(), spec)

      // assert
      result shouldNotBe null
    }
    it("Can deserialize the digital ocean spec") {
      // arrange
      val spec = readFile("digital_ocean.json")

      // act
      val result = json.decodeFromString(OpenApi.serializer(), spec)

      // assert
      result shouldNotBe null
    }
    it("Can deserialize the neon spec") {
      // arrange
      val spec = readFile("neon.json")

      // act
      val result = json.decodeFromString(OpenApi.serializer(), spec)

      // assert
      result shouldNotBe null
    }
  }
}) {
  companion object {
    val json = Json {
      ignoreUnknownKeys = true
      isLenient = true
    }

    fun readFile(path: String) = this::class.java.classLoader
      ?.getResource(path)
      ?.readText()
      ?: error("Unable to locate file 👀")
  }
}
