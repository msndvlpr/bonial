package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Page(

    @SerialName("size")
    val size: Int? = null,

    @SerialName("totalElements")
    val totalElements: Int? = null,

    @SerialName("totalPages")
    val totalPages: Int? = null,

    @SerialName("number")
    val number: Int? = null

)