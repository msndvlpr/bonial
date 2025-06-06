package com.bonial.codechallenge.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bonial.codechallenge.data.repositpry.advertisement.AdvertisementRepository
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentItem
import com.bonial.codechallenge.ui.ViewState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class BrochureViewModel @Inject constructor(private val advertisementRepository: AdvertisementRepository) :
    ViewModel() {

    private val _viewState: MutableStateFlow<ViewState<List<ContentItem>>?> = MutableStateFlow(null)
    val viewState: StateFlow<ViewState<List<ContentItem>>?> = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            advertisementRepository.advertisementsFlow.collect { adsData ->
                adsData?.onSuccess { data ->
                    if(data != null){
                        _viewState.value = ViewState.Success(data)
                    } else {
                        _viewState.value = ViewState.Failure
                    }

                }?.onFailure {
                    _viewState.value = ViewState.Failure
                }

            }
        }
    }

    fun onUIEvent(event: UiEvent) {
        when (event) {

            UiEvent.OnInit -> {
                _viewState.value = ViewState.Loading
                triggerAdvertisementDataItemsFetch()
            }

            UiEvent.OnClose -> {
                //todo: to be implemented if needed
            }

            else -> {}
        }
    }

    fun retryLoadData(){
        triggerAdvertisementDataItemsFetch()
        _viewState.value = ViewState.Loading
        Timber.d("retry triggered")
    }

    private fun triggerAdvertisementDataItemsFetch() {
        viewModelScope.launch(Dispatchers.IO) {
            advertisementRepository.getAllAdvertisementItems()
        }
    }

    sealed interface UiEvent {
        object OnInit : UiEvent

        object OnClose : UiEvent
    }
}