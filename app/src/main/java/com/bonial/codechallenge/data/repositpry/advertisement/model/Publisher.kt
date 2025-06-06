package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Publisher (

  @SerialName("id")
  val id : String? = null,

  @SerialName("name")
  val name : String? = null,

  @SerialName("type" )
  val type : String? = null

)