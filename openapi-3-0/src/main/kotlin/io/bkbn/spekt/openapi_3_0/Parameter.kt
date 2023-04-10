package io.bkbn.spekt.openapi_3_0

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject

object ParameterSerializer : JsonContentPolymorphicSerializer<Parameter>(Parameter::class) {
  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Parameter> = when {
    element.jsonObject.containsKey("\$ref") -> ReferenceParameter.serializer()
    else -> LiteralParameter.serializer()
  }
}

@Serializable(with = ParameterSerializer::class)
sealed interface Parameter

@Serializable
data class LiteralParameter(
  val `in`: String,
  val name: String,
  val schema: Schema,
  val description: String? = null,
  val required: Boolean = false,
  val deprecated: Boolean = false,
) : Parameter

@Serializable
data class ReferenceParameter(
  val `$ref`: String,
) : Parameter
