package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName


@Serializable
data class Embedded (

  @SerialName("contents")
  val contentItems : ArrayList<ContentItem> = arrayListOf()

)