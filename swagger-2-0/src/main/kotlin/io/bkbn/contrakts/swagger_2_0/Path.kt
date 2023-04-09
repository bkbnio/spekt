package io.bkbn.contrakts.swagger_2_0

import kotlinx.serialization.Serializable

@Serializable
data class Path(
  val get: Operation? = null,
) {
  @Serializable
  data class Operation(
    val summary: String,
    val description: String? = null,
    val operationId: String? = null,
    val produces: List<String> = emptyList(),
    val parameters: List<Parameter> = emptyList(),
  )

  @Serializable
  data class Parameter(
    val name: String,
    val `in`: String,
    val description: String? = null,
    val required: Boolean = false,
    val type: String,
  )
}
