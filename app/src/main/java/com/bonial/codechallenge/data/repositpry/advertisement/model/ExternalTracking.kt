package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ExternalTracking(

    @SerialName("impression")
    val impression: ArrayList<String> = arrayListOf(),

    @SerialName("click")
    val click: ArrayList<String> = arrayListOf()

)