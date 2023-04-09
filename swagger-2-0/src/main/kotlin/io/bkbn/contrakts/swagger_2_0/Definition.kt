package io.bkbn.contrakts.swagger_2_0

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

object DefinitionSerializer : JsonContentPolymorphicSerializer<Definition>(Definition::class) {
  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Definition> = when {
    "\$ref" in element.jsonObject -> ReferenceDefinition.serializer()
    "allOf" in element.jsonObject -> AllOfDefinition.serializer()
    "type" in element.jsonObject -> {
      when (element.jsonObject["type"]?.jsonPrimitive?.content) {
        "object" -> ObjectDefinition.serializer()
        "array" -> ArrayDefinition.serializer()
        "string" -> StringDefinition.serializer()
        "integer" -> IntegerDefinition.serializer()
        "number" -> NumberDefinition.serializer()
        "boolean" -> BooleanDefinition.serializer()
        else -> throw IllegalArgumentException("Unknown type ${element.jsonObject}")
      }
    }
    else -> throw IllegalArgumentException("Unknown definition ${element.jsonObject}")
  }
}

@Serializable(with = DefinitionSerializer::class)
sealed interface Definition {
  val description: String?
}

@SerialName("object")
@Serializable
data class ObjectDefinition(
  val properties: Map<String, Definition> = emptyMap(),
  val required: List<String> = emptyList(),
  override val description: String? = null
) : Definition

@SerialName("array")
@Serializable
data class ArrayDefinition(
  val items: Definition,
  override val description: String? = null
) : Definition

@SerialName("string")
@Serializable
data class StringDefinition(
  val format: String? = null,
  val enum: List<String>? = null,
  override val description: String? = null
) : Definition

@SerialName("integer")
@Serializable
data class IntegerDefinition(
  val format: String? = null,
  override val description: String? = null
) : Definition

@SerialName("number")
@Serializable
data class NumberDefinition(
  val format: String? = null,
  override val description: String? = null
) : Definition

@SerialName("boolean")
@Serializable
data class BooleanDefinition(
  override val description: String? = null
) : Definition

@Serializable
data class ReferenceDefinition(
  val `$ref`: String
) : Definition {
  override val description: String? = null
}

@Serializable
data class AllOfDefinition(
  val allOf: List<Definition>
) : Definition {
  override val description: String? = null
}

