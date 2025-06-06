package com.bonial.codechallenge.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentItem
import com.bonial.codechallenge.ui.ViewState


@Composable
fun BrochureScreen(viewModel: BrochureViewModel = hiltViewModel()) {
    val viewState by viewModel.viewState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.onUIEvent(BrochureViewModel.UiEvent.OnInit)
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (viewState) {
            is ViewState.Loading -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            is ViewState.Failure -> {
                ErrorScreen { viewModel.retryLoadData() }
            }

            is ViewState.Success -> {
                val brochures = (viewState as ViewState.Success<List<ContentItem>>).data

                if (brochures.isNotEmpty()) {
                    BrochureGrid(brochures)
                } else {
                    EmptyStateScreen()
                }
            }

            null -> {}
        }
    }
}
