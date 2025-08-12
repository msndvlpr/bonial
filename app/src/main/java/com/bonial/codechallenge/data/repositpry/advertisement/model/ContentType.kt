package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
enum class ContentType {

  @SerialName("brochure")
  BROCHURE,

  @SerialName("brochurePremium")
  BROCHURE_PREMIUM,

  @SerialName("superBannerCarousel")
  SUPER_BANNER_CAROUSEL
}