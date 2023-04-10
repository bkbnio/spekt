package io.bkbn.spek.swagger_2_0

import kotlinx.serialization.Serializable

@Serializable
data class Path(
  val get: Operation? = null,
  val put: Operation? = null,
  val post: Operation? = null,
  val delete: Operation? = null,
  val options: Operation? = null,
  val head: Operation? = null,
  val patch: Operation? = null,
//  val parameters: List<Parameter> = emptyList(),
) {
  @Serializable
  data class Operation(
    val summary: String,
    val description: String? = null,
    val operationId: String? = null,
    val produces: List<String> = emptyList(),
//    val parameters: List<Parameter> = emptyList(),
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
