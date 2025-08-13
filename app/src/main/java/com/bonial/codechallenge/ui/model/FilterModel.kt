package com.bonial.codechallenge.ui.model

import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentType


data class FilterModel(
    val contentTypeFilter: List<ContentType> = listOf(ContentType.BROCHURE, ContentType.BROCHURE_PREMIUM),

    val distanceFilter: Double = 5.0,

    val refreshKey: Long = 0 // for reload purpose
)
