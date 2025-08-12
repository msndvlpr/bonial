package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Page(

    val size: Int? = null,

    val totalElements: Int? = null,

    val totalPages: Int? = null,

    val number: Int? = null

)