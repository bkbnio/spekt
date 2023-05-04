package io.bkbn.spekt.playground

import io.bkbn.spekt.api.client.codegen.ApiClientGenerator
import io.bkbn.spekt.openapi_3_0.OpenApi
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import java.nio.file.Path

suspend fun main() {
  val client = HttpClient(CIO) {
    install(Auth) {
      bearer {
        loadTokens {
          BearerTokens("abc123", "xyz111")
        }
      }
    }
    install(ContentNegotiation) {
      json(Json {
        ignoreUnknownKeys = true
        prettyPrint = true
        isLenient = true
      })
    }
  }
  val spec: OpenApi = client.get("https://dfv3qgd2ykmrx.cloudfront.net/api_spec/release/v2.json").body()
  Path.of("./playground/src/gen").toFile().deleteRecursively()
  ApiClientGenerator.generate(spec).forEach {
    it.writeTo(Path.of("./playground/src/gen/kotlin"))
  }
}
