package io.bkbn.spek.openapi_3_0

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
  val name: String,
  val description: String? = null,
)
