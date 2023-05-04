package io.bkbn.spekt.api.client.codegen

import com.squareup.kotlinpoet.AnnotationSpec
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeAliasSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asClassName
import io.bkbn.spekt.openapi_3_0.AllOfSchema
import io.bkbn.spekt.openapi_3_0.AnyOfSchema
import io.bkbn.spekt.openapi_3_0.ArraySchema
import io.bkbn.spekt.openapi_3_0.BooleanSchema
import io.bkbn.spekt.openapi_3_0.FreeFormSchema
import io.bkbn.spekt.openapi_3_0.IntegerSchema
import io.bkbn.spekt.openapi_3_0.ObjectSchema
import io.bkbn.spekt.openapi_3_0.OneOfSchema
import io.bkbn.spekt.openapi_3_0.OpenApi
import io.bkbn.spekt.openapi_3_0.Path
import io.bkbn.spekt.openapi_3_0.ReferenceSchema
import io.bkbn.spekt.openapi_3_0.Schema
import io.bkbn.spekt.openapi_3_0.StringSchema
import java.util.Locale

internal object OpenApiClientGenerator {
  fun generate(spek: OpenApi): List<FileSpec> {
    val models = generateModels(spek)
    val requests = generateRequests(spek)
    return models + requests
  }

  private fun generateModels(spek: OpenApi): List<FileSpec> = spek.components.schemas.map { (name, schema) ->
    FileSpec.builder("io.bkbn.spekt.api.client.models", name).apply {
      generateModelFromSchema(spek, name, schema)
    }.build()
  }

  // TODO Clean this up
  private fun FileSpec.Builder.generateModelFromSchema(parentSpec: OpenApi, name: String, schema: Schema) {
    when (schema) {
      is AllOfSchema -> generateAllOfSchema(parentSpec, schema, name)
      is AnyOfSchema -> {}
      is ArraySchema -> {}
      is BooleanSchema -> {}
      FreeFormSchema -> error("FreeFormSchema is not currently supported")
      is IntegerSchema -> {
        addTypeAlias(TypeAliasSpec.builder(name, Int::class.asClassName()).build())
      }

      is ObjectSchema -> generateObjectSchema(name, schema)
      is OneOfSchema -> {}
      is ReferenceSchema -> {}
      is StringSchema -> {
        // TODO Date and DateTime and stuff?
        if (schema.enum?.isNotEmpty() == true) {
          addType(TypeSpec.enumBuilder(name).apply {
            schema.enum?.filterNotNull()?.forEach { enumValue ->
              if (enumValue.isSnake() || enumValue.isCamel()) {
                val formattedValue = enumValue.toAngrySnake()
                addEnumConstant(formattedValue, TypeSpec.anonymousClassBuilder().apply {
                  addAnnotation(
                    AnnotationSpec.builder(ClassName("kotlinx.serialization", "SerialName"))
                      .addMember("%S", enumValue).build()
                  )
                }.build())
              } else {
                addEnumConstant(enumValue)
              }
            }
          }.build())
        } else {
          addTypeAlias(TypeAliasSpec.builder(name, String::class.asClassName()).build())
        }
      }
    }
  }

  private fun FileSpec.Builder.generateAllOfSchema(parentSpec: OpenApi, schema: AllOfSchema, name: String) {
    val allOf = schema.allOf
    require(allOf.all { it is ReferenceSchema }) { "Currently, all members of an allOf schema must be ReferenceSchemas" }
    val references = allOf.map { it as ReferenceSchema }.map { it.`$ref` }.map { it.substringAfterLast("/") }
    val schemas = references.map { parentSpec.components.schemas[it]!! }
    require(schemas.all { it is ObjectSchema }) { "Currently, references within an allOf schema must point to ObjectSchemas" }
    val objectSchemas = schemas.map { it as ObjectSchema }
    val gigaSchema = ObjectSchema(
      properties = objectSchemas.map { it.properties }.flatMap { it.entries }.associate { it.key to it.value },
      required = objectSchemas.flatMap { it.required }.toList()
    )
    generateObjectSchema(name, gigaSchema)
  }

  private fun FileSpec.Builder.generateObjectSchema(name: String, schema: ObjectSchema) {
    if (schema.properties.isEmpty()) {
      addType(TypeSpec.objectBuilder(name).apply {
        addAnnotation(AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable")).build())
      }.build())
      return
    }
    addType(TypeSpec.classBuilder(name).apply {
      addModifiers(KModifier.DATA)
      addAnnotation(AnnotationSpec.builder(ClassName("kotlinx.serialization", "Serializable")).build())
      primaryConstructor(
        FunSpec.constructorBuilder().apply {
          schema.properties.forEach { (propName, propSchema) ->
            val sanitizedName = propName.sanitizePropertyName()
            val valName = if (sanitizedName.isSnake()) sanitizedName.snakeToCamel() else sanitizedName
            addParameter(valName, propSchema.toTypeName())
          }
        }.build()
      )
      schema.properties.forEach { (propName, propSchema) ->
        val sanitizedName = propName.sanitizePropertyName()
        val valName = if (sanitizedName.isSnake()) sanitizedName.snakeToCamel() else sanitizedName
        addProperty(PropertySpec.builder(valName, propSchema.toTypeName()).apply {
          initializer(valName)
          if (valName != propName) {
            addAnnotation(AnnotationSpec.builder(ClassName("kotlinx.serialization", "SerialName")).apply {
              addMember("\"$propName\"")
            }.build())
          }
        }.build())
      }
    }.build())
  }

  private fun Schema.toTypeName(): TypeName = when (this) {
    is AllOfSchema -> String::class.asClassName() // TODO Placeholder
    is AnyOfSchema -> String::class.asClassName() // TODO Placeholder
    is ArraySchema -> List::class.asClassName().parameterizedBy(items.toTypeName())
    is BooleanSchema -> Boolean::class.asClassName()
    FreeFormSchema -> error("FreeFormSchema is not currently supported")
    is IntegerSchema -> Int::class.asClassName()
    is ObjectSchema -> String::class.asClassName() // TODO Placeholder
    is OneOfSchema -> String::class.asClassName() // TODO Placeholder
    is ReferenceSchema -> ClassName("io.bkbn.spekt.api.client.models", `$ref`.substringAfterLast("/"))
    is StringSchema -> String::class.asClassName()
  }

  private fun generateRequests(spek: OpenApi): List<FileSpec> = spek.paths.map { (slug, path) ->
    listOfNotNull(
      path.get?.toRequest(slug, HttpMethod.GET),
      path.put?.toRequest(slug, HttpMethod.PUT),
      path.post?.toRequest(slug, HttpMethod.POST),
      path.delete?.toRequest(slug, HttpMethod.DELETE),
      path.options?.toRequest(slug, HttpMethod.OPTIONS),
      path.head?.toRequest(slug, HttpMethod.HEAD),
      path.patch?.toRequest(slug, HttpMethod.PATCH),
      path.trace?.toRequest(slug, HttpMethod.TRACE)
    )
  }.flatten()

  private fun Path.Operation.toRequest(slug: String, httpMethod: HttpMethod): FileSpec {
    val name = operationId?.capitalized() ?: error("Currently an operation id is required")
    return FileSpec.builder("io.bkbn.spekt.api.client.requests", name).apply {
      println("$slug $httpMethod")
    }.build()
  }

  private fun String.capitalized() =
    replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

  private fun String.isSnake() = matches(Regex("^[a-z]+(_[a-z]+)+$"))

  private fun String.isCamel() = matches(Regex("^[a-z]+(?:[A-Z][a-z]*)*$"))

  private fun String.snakeToCamel() = split("_").mapIndexed { index, word ->
    if (index == 0) word
    else word.capitalized()
  }.joinToString("")

  private fun String.toAngrySnake(): String {
    if (!isSnake() && !isCamel()) {
      throw IllegalArgumentException("The provided string is neither in snake_case nor camelCase.")
    }

    val snakeCaseValue = if (isCamel()) {
      replace(Regex("([A-Z])"), "_$1").lowercase(Locale.getDefault())
    } else {
      this
    }

    return snakeCaseValue.uppercase(Locale.getDefault())
  }

  private fun String.sanitizePropertyName(): String = trim().replace(Regex("\\s+"), "_")
}
