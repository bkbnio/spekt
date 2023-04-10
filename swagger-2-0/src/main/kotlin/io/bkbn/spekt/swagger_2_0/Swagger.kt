package io.bkbn.spekt.swagger_2_0

import kotlinx.serialization.Serializable

@Serializable
data class Swagger(
  val swagger: String,
  val schemes: List<String>,
  val produces: List<String>, // TODO Enum?
  val consumes: List<String>, // TODO Enum?
  val basePath: String,
  val info: Info,
  val tags: List<Tag>,
  val definitions: Map<String, Definition>,
  val paths: Map<String, Path>
)
