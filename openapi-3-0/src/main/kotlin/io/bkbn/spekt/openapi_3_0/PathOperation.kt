package io.bkbn.spekt.openapi_3_0

import kotlinx.serialization.Serializable

@Serializable
data class PathOperation(
    val summary: String,
    val description: String? = null,
    val tags: List<String> = emptyList(),
    val operationId: String? = null,
    val parameters: List<Parameter> = emptyList(),
    val requestBody: RequestBody? = null,
    val responses: Map<String, Response> = emptyMap(),
)
