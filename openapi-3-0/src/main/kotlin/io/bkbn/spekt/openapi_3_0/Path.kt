package io.bkbn.spekt.openapi_3_0

import kotlinx.serialization.Serializable

@Serializable
data class Path(
  val get: PathOperation? = null,
  val put: PathOperation? = null,
  val post: PathOperation? = null,
  val delete: PathOperation? = null,
  val options: PathOperation? = null,
  val head: PathOperation? = null,
  val patch: PathOperation? = null,
  val trace: PathOperation? = null,
  val parameters: List<Parameter> = emptyList(),
)
