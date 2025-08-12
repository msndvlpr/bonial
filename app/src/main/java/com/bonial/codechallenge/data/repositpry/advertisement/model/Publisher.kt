package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Publisher (

  val id : String? = null,

  val name : String? = null,

  val type : String? = null

)