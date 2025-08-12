package com.bonial.codechallenge.ui.home

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonial.codechallenge.data.repositpry.advertisement.AdvertisementRepository
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentItem
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentType
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentVariant
import com.bonial.codechallenge.ui.ViewState
import com.bonial.codechallenge.ui.model.BrochureUiModel
import com.bonial.codechallenge.ui.model.FilterModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch



@HiltViewModel
class BrochureViewModel @Inject constructor(private val advertisementRepository: AdvertisementRepository) :
    ViewModel() {

    private val _filter: MutableStateFlow<FilterModel> = MutableStateFlow(FilterModel())
    val filter: StateFlow<FilterModel> = _filter.asStateFlow()


    @OptIn(ExperimentalCoroutinesApi::class)
    val viewState: StateFlow<ViewState<List<BrochureUiModel>>> =
        _filter
            .onStart { emit(FilterModel()) } // ensure initial emission
            .flatMapLatest { filter ->
                flow {
                    emit(ViewState.Loading)

                    // Trigger repository to fetch filtered data
                    advertisementRepository.getAllAdvertisementItems(
                        filter.contentTypeFilter,
                        filter.distanceFilter
                    )

                    // Collect the updated data
                    advertisementRepository.advertisementsFlow
                        .filterNotNull()
                        .map { result ->
                            result.fold(
                                onSuccess = { data ->
                                    if (data != null) {
                                        ViewState.Success(data.map { it.toUiModel() })
                                    } else {
                                        ViewState.Failure
                                    }
                                },
                                onFailure = { ViewState.Failure }
                            )
                        }
                        .collect { emit(it) }
                }
            }
            .stateIn(viewModelScope, SharingStarted.Lazily, ViewState.Loading)


    init {
        viewModelScope.launch {
            advertisementRepository.getAllAdvertisementItems(
                _filter.value.contentTypeFilter,
                _filter.value.distanceFilter
            )
        }
    }

    fun updateContentTypeFilter(newContentTypes: List<ContentType>) {
        _filter.value = _filter.value.copy(contentTypeFilter = newContentTypes)
    }

    // To be used when UI component is implemented
    fun updateDistanceFilter(newDistance: Double) {
        _filter.value = _filter.value.copy(distanceFilter = newDistance)
    }

    @VisibleForTesting
    fun ContentItem.toUiModel(): BrochureUiModel {
        return BrochureUiModel(
            id = (this.content as ContentVariant.Brochure).id ?: 0,
            retailerName = this.content.publisher?.name ?: "-",
            type = this.contentType ?: ContentType.BROCHURE,
            formattedDistance = formatDistance(this.content.distance ?: 0.0),
            formattedExpiry = formatExpiry(),
            imageUrl = this.content.brochureImage,
            isFavourite = false
        )
    }

    @VisibleForTesting
    fun formatDistance(distanceKm: Double): String {
        return if (distanceKm >= 1) {
            "${"%.2f".format(distanceKm)} km"
        } else {
            "${(distanceKm * 1000).toInt()} m"
        }
    }

    // Generate random number for simulating expiry days
    @VisibleForTesting
    fun formatExpiry(): String {
        val daysToExpire = (1..29).random()
        return "Expires in $daysToExpire days"
    }

    fun retryLoadData(){
        _filter.value = _filter.value.copy(
            distanceFilter = _filter.value.distanceFilter,
            contentTypeFilter = _filter.value.contentTypeFilter,
            refreshKey = System.currentTimeMillis(),
            )
    }
}