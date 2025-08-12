package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.Serializable

@Serializable
sealed class ContentVariant {

    @Serializable
    data class SuperBannerList(
        val items: List<CarouselContent> ) : ContentVariant()

    @Serializable
    data class Brochure(
        val id: Long? = null,

        val contentId: String? = null,

        val title: String? = null,

        val validFrom: String? = null,

        val validUntil: String? = null,

        val publishedFrom: String? = null,

        val publishedUntil: String? = null,

        val type: String? = null,

        val brochureImage: String? = null,

        val pageCount: Int? = null,

        val publisher: Publisher? = Publisher(),

        val contentBadges: ArrayList<ContentBadge>? = arrayListOf(),

        val distance: Double? = null,

        val hideValidityDate: Boolean? = null,

        val closestStore: ClosestStore? = ClosestStore() ) : ContentVariant()
}