package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class NetworkResponse(

    @SerialName("_embedded")
    val embedded: Embedded? = Embedded(),

    @SerialName("_links")
    val links: Links? = Links(),

    val page: Page? = Page()

)