package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Links (

  @SerialName("self" )
  val self : Self? = Self()

)