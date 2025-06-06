package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BrochureContent(

    @SerialName("id")
    val id: Long? = null,

    @SerialName("contentId")
    val contentId: String? = null,

    @SerialName("title")
    val title: String? = null,

    @SerialName("validFrom")
    val validFrom: String? = null,

    @SerialName("validUntil")
    val validUntil: String? = null,

    @SerialName("publishedFrom")
    val publishedFrom: String? = null,

    @SerialName("publishedUntil")
    val publishedUntil: String? = null,

    //todo
    @SerialName("type")
    val type: String? = null,

    @SerialName("brochureImage")
    val brochureImage: String? = null,

    @SerialName("pageCount")
    val pageCount: Int? = null,

    @SerialName("publisher")
    val publisher: Publisher? = Publisher(),

    @SerialName("contentBadges")
    val contentBadges: ArrayList<ContentBadge>? = arrayListOf(),

    @SerialName("distance")
    val distance: Double? = null,

    @SerialName("hideValidityDate")
    val hideValidityDate: Boolean? = null,

    @SerialName("closestStore")
    val closestStore: ClosestStore? = ClosestStore()

)
