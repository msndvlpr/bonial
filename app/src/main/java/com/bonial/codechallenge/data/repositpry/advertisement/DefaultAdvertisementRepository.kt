package com.bonial.codechallenge.data.repositpry.advertisement

import com.bonial.codechallenge.data.datasource.remote.NetworkDataSource
import com.bonial.codechallenge.data.repositpry.advertisement.model.NetworkResponse
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentType
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import timber.log.Timber
import javax.inject.Inject

internal class DefaultAdvertisementRepository @Inject constructor(
    private val networkDataSource: NetworkDataSource

    ) : AdvertisementRepository {

    private val _advertisementsFlow = MutableStateFlow<Result<List<ContentItem>?>?>(null)
    override val advertisementsFlow: StateFlow<Result<List<ContentItem>?>?> = _advertisementsFlow


    override suspend fun getAllAdvertisementItems() {

        val networkResponse: Result<NetworkResponse> = networkDataSource.getAllAdvertisements()

        networkResponse.onSuccess {
            val ads = it.embedded?.contentItems?.filter { content -> content.contentType in listOf(ContentType.BROCHURE, ContentType.BROCHURE_PREMIUM) }

            _advertisementsFlow.emit(Result.success(ads))

        }.onFailure {
            Timber.e(it.message)
            _advertisementsFlow.emit(Result.failure(it))
        }
    }


}