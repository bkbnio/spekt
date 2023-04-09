package io.bkbn.spek.openapi_3_0

import kotlinx.serialization.Serializable

@Serializable
data class Info(
  val version: String,
  val title: String,
  val description: String? = null,
  val termsOfService: String? = null,
  val contact: Contact? = null,
  val license: License? = null
) {
  @Serializable
  data class Contact(
    val name: String? = null,
    val url: String? = null,
    val email: String? = null
  )

  @Serializable
  data class License(
    val name: String,
    val url: String? = null
  )
}
