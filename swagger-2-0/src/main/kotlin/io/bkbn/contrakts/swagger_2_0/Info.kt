package io.bkbn.contrakts.swagger_2_0

import kotlinx.serialization.Serializable

@Serializable
data class Info(
  val title: String,
  val version: String,
  val description: String? = null,
)
