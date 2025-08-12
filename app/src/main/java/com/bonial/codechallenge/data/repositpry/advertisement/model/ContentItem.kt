package com.bonial.codechallenge.data.repositpry.advertisement.model

import com.bonial.codechallenge.data.repositpry.advertisement.ContentVariantSerializer
import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ContentItem(

    val placement: String? = null,

    val adFormat: String? = null,

    val contentFormatSource: String? = null,

    val contentType: ContentType? = null,

    //polymorphic attribute
    @Serializable(with = ContentVariantSerializer::class)
    val content: ContentVariant? = null,

    val externalTracking: ExternalTracking? = ExternalTracking()

)