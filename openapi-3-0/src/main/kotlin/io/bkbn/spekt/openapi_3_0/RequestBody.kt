package io.bkbn.spekt.openapi_3_0

import kotlinx.serialization.Serializable

@Serializable
data class RequestBody(
    val required: Boolean? = null,
    val content: Map<String, Content> = emptyMap(),
)
