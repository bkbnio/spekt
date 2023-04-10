package io.bkbn.spekt.openapi_3_0

import kotlinx.serialization.Serializable

@Serializable
data class Components(
  val schemas: Map<String, Schema> = emptyMap(),
  val parameters: Map<String, Parameter> = emptyMap(),
)
