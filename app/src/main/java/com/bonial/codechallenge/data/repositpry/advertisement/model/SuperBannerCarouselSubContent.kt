package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class SuperBannerCarouselSubContent(
    @SerialName("id")
    val id: String? = null,

    @SerialName("publisher")
    val publisher: Publisher? = Publisher(),

    @SerialName("publishedFrom")
    val publishedFrom: String? = null,

    @SerialName("publishedUntil")
    val publishedUntil: String? = null,

    @SerialName("clickUrl")
    val clickUrl: String? = null,

    @SerialName("imageUrl")
    val imageUrl: String? = null
    )