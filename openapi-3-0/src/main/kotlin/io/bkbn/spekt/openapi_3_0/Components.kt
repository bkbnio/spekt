package io.bkbn.spekt.openapi_3_0

import kotlinx.serialization.Serializable

@Serializable
data class Components(
  val schemas: Map<String, Schema> = emptyMap(),
  val parameters: Map<String, Parameter> = emptyMap(),
  val securitySchemes: Map<String, SecurityScheme> = emptyMap(),
  val requestBodies: Map<String, RequestBody> = emptyMap(),
  val responses: Map<String, Response> = emptyMap(),
  // TODO
  // val headers: Map<String, Header> = emptyMap(),
)
