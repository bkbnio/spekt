package io.bkbn.spek.openapi_3_0

import kotlinx.serialization.Serializable

@Serializable
data class Server(
  val url: String,
)
