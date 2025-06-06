package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ContentItem(

    @SerialName("placement")
    val placement: String? = null,

    @SerialName("adFormat")
    val adFormat: String? = null,

    @SerialName("contentFormatSource")
    val contentFormatSource: String? = null,

    @SerialName("contentType")
    val contentType: ContentType? = null,

    @SerialName("content")
    val content: BrochureContent? = null,

    @SerialName("externalTracking")
    val externalTracking: ExternalTracking? = ExternalTracking()

)