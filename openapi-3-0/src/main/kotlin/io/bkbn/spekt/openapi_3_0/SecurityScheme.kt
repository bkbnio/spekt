@file:OptIn(ExperimentalSerializationApi::class)

package io.bkbn.spekt.openapi_3_0

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object SecuritySchemeSerializer : JsonContentPolymorphicSerializer<SecurityScheme>(SecurityScheme::class) {
  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<SecurityScheme> =
    when (element.jsonObject["type"]?.jsonPrimitive?.content) {
      "http" -> when (element.jsonObject["scheme"]?.jsonPrimitive?.content) {
        "basic" -> BasicAuthSecurityScheme.serializer()
        "bearer" -> BearerAuthSecurityScheme.serializer()
        else -> throw IllegalArgumentException("Unknown http scheme ${element.jsonObject}")
      }

      "apiKey" -> ApiKeySecurityScheme.serializer()
      "openIdConnect" -> OpenIdConnectScheme.serializer()
      "oauth2" -> OAuth2SecurityScheme.serializer()
      else -> throw IllegalArgumentException("Unknown security scheme ${element.jsonObject}")
    }
}

@Serializable(with = SecuritySchemeSerializer::class)
sealed interface SecurityScheme

@Serializable
data class BasicAuthSecurityScheme(val type: String, val scheme: String) : SecurityScheme

@Serializable
data class BearerAuthSecurityScheme(val type: String, val scheme: String) : SecurityScheme

@Serializable
data class ApiKeySecurityScheme(
  val name: String,
  val `in`: String,
) : SecurityScheme

@Serializable
data class OpenIdConnectScheme(
  val openIdConnectUrl: String,
) : SecurityScheme

@Serializable
data class OAuth2SecurityScheme(
  val flows: Map<String, Flow>,
) : SecurityScheme {
  @Serializable
  data class Flow(
    val authorizationUrl: String? = null,
    val tokenUrl: String? = null,
    val refreshUrl: String? = null,
    val scopes: Map<String, String> = emptyMap(),
  )
}
