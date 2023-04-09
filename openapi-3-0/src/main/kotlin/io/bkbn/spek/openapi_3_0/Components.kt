package io.bkbn.spek.openapi_3_0

import kotlinx.serialization.Serializable

@Serializable
data class Components(
  val schemas: Map<String, Schema> = emptyMap(),
)
