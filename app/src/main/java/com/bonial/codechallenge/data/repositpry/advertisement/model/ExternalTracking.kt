package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ExternalTracking(

    val impression: List<String> = listOf(),

    val click: List<String> = listOf()

)