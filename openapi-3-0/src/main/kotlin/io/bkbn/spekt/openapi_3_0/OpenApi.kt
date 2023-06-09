package io.bkbn.spekt.openapi_3_0

import io.bkbn.spekt.common.Spek
import kotlinx.serialization.Serializable

@Serializable
data class OpenApi(
  val openapi: String = "3.0.3",
  val info: Info,
  val servers: List<Server> = emptyList(),
  val paths: Map<String, Path> = emptyMap(),
  val components: Components = Components(),
  val security: List<Map<String, List<String>>> = emptyList(),
  val tags: List<Tag> = listOf(),
  val externalDocs: ExternalDocumentation? = null
) : Spek
