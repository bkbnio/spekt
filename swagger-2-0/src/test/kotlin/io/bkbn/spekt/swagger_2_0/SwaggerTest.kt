package io.bkbn.spekt.swagger_2_0

import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldNotBe
import kotlinx.serialization.json.Json

class SwaggerTest : DescribeSpec({
  describe("File Utils") {
    it("Can deserialize the docker spec") {
      // arrange
      val spec = readFile("docker.json")

      // act
      val result = json.decodeFromString(Swagger.serializer(), spec)

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
