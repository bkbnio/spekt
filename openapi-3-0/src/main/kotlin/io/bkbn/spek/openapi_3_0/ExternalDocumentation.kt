package io.bkbn.spek.openapi_3_0

import kotlinx.serialization.Serializable

@Serializable
data class ExternalDocumentation(
  val description: String? = null,
  val url: String,
)
