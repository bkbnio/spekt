package io.bkbn.spek.openapi_3_0

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
  val trace: Operation? = null,
) {
  @Serializable
  data class Operation(
    val summary: String,
    val description: String? = null,
    val tags: List<String> = emptyList(),
    val operationId: String? = null,
    val parameters: List<Parameter> = emptyList(),
    val requestBody: RequestBody? = null,
    val responses: Map<String, Response> = emptyMap(),
  )

  @Serializable
  data class Parameter(
    val name: String? = null,
  )

  @Serializable
  data class RequestBody(
    val required: Boolean? = null,
  )

  @Serializable
  data class Response(
    val description: String? = null,
    val content: Map<String, Content> = emptyMap(),
  )

  @Serializable
  data class Content(
    val schema: Schema? = null,
  )
}
