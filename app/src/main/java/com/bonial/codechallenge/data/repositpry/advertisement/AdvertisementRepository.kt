package com.bonial.codechallenge.data.repositpry.advertisement

import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentItem
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentType
import kotlinx.coroutines.flow.StateFlow

interface AdvertisementRepository {

    val advertisementsFlow: StateFlow<Result<List<ContentItem>?>?>

    suspend fun getAllAdvertisementItems(contentType: List<ContentType>, distance: Double)

}