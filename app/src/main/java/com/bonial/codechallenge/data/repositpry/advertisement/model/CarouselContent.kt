package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class CarouselContent(

    val content: CarouselSubContent? = CarouselSubContent(),

    val externalTracking: ExternalTracking? = ExternalTracking()
    )