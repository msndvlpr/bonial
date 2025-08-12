package com.bonial.codechallenge.ui.model

import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentType
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentVariant

data class BrochureUiModel(
    val id: Long,

    val retailerName: String,

    val type: ContentType,

    val formattedDistance: String,

    val formattedExpiry: String,

    val imageUrl: String?,

    val isFavourite: Boolean = false
)

