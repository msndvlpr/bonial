package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CarouselSubContent(

    val id: String? = null,

    val publisher: Publisher? = Publisher(),

    val publishedFrom: String? = null,

    val publishedUntil: String? = null,

    val clickUrl: String? = null,

    val imageUrl: String? = null
    )