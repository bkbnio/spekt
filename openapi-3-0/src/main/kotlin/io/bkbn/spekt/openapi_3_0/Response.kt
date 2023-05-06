package io.bkbn.spekt.openapi_3_0

import kotlinx.serialization.Serializable

@Serializable
data class Response(
  val description: String? = null,
  val content: Map<String, Content> = emptyMap(),
)
