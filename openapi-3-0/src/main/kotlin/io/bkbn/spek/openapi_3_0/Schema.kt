package io.bkbn.spek.openapi_3_0

import kotlinx.serialization.DeserializationStrategy
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonContentPolymorphicSerializer
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive

@Suppress("CyclomaticComplexMethod")
object SchemaSerializer : JsonContentPolymorphicSerializer<Schema>(Schema::class) {
  override fun selectDeserializer(element: JsonElement): DeserializationStrategy<Schema> =
    when {
      element is JsonPrimitive -> FreeFormSchema.serializer()
      element is JsonObject && element.toString() == "{}" -> FreeFormSchema.serializer()
      "\$ref" in element.jsonObject -> ReferenceSchema.serializer()
      "allOf" in element.jsonObject -> AllOfSchema.serializer()
      "anyOf" in element.jsonObject -> AnyOfSchema.serializer()
      "oneOf" in element.jsonObject -> OneOfSchema.serializer()
      "type" in element.jsonObject -> {
        when (element.jsonObject["type"]?.jsonPrimitive?.content) {
          "object" -> ObjectSchema.serializer()
          "array" -> ArraySchema.serializer()
          "string" -> StringSchema.serializer()
          "integer" -> IntegerSchema.serializer()
          "number" -> IntegerSchema.serializer()
          "boolean" -> BooleanSchema.serializer()
          // TODO This is a hack cuz the github api has some malformed data.. should throw error
          else -> StringSchema.serializer()
        }
      }
      // TODO This is a hack cuz the github api has some malformed data.. should throw error
      else -> StringSchema.serializer()
    }
}

@Serializable(with = SchemaSerializer::class)
sealed interface Schema

@Serializable
data class StringSchema(
  val format: String? = null,
  val enum: List<String?>? = null,
  val nullable: Boolean? = null,
) : Schema

@Serializable
data class IntegerSchema(
  val format: String? = null,
  val nullable: Boolean? = null,
) : Schema

@Serializable
data class BooleanSchema(
  val nullable: Boolean? = null,
) : Schema

@Serializable
data class ArraySchema(
  val items: Schema,
  val nullable: Boolean? = null,
) : Schema

@Serializable
data class ReferenceSchema(
  val `$ref`: String,
) : Schema

@Serializable
data class AllOfSchema(
  val allOf: List<Schema>,
  val nullable: Boolean? = null,
) : Schema

@Serializable
data class OneOfSchema(
  val oneOf: List<Schema>,
  val nullable: Boolean? = null,
) : Schema

@Serializable
data class AnyOfSchema(
  val anyOf: List<Schema>,
  val nullable: Boolean? = null,
) : Schema

@Serializable
data class ObjectSchema(
  val title: String? = null,
  val description: String? = null,
  val properties: Map<String, Schema> = emptyMap(),
  val required: List<String> = emptyList(),
  // TODO This is a pain in the ass cuz it can be an object or a boolean 🙄
  // val additionalProperties: Schema? = null,
  val nullable: Boolean? = null,
) : Schema

@Serializable
object FreeFormSchema : Schema
