package io.bkbn.spekt.api.client.codegen

import com.squareup.kotlinpoet.FileSpec
import io.bkbn.spekt.common.Spek
import io.bkbn.spekt.openapi_3_0.OpenApi
import io.bkbn.spekt.swagger_2_0.Swagger

object ApiClientGenerator {
  fun generate(spek: Spek): List<FileSpec> = when (spek) {
    is OpenApi -> OpenApiClientGenerator.generate(spek)
    is Swagger -> SwaggerClientGenerator.generate(spek)
    else -> throw IllegalArgumentException("Unknown Spek type: ${spek::class.simpleName}")
  }
}
