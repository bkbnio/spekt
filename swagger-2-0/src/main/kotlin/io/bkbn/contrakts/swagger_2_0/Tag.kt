package io.bkbn.contrakts.swagger_2_0

import kotlinx.serialization.Serializable

@Serializable
data class Tag(
  val name: String,
  val description: String? = null,
)
