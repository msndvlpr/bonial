package com.bonial.codechallenge.ui.home

import com.bonial.codechallenge.data.repositpry.advertisement.AdvertisementRepository
import com.bonial.codechallenge.data.repositpry.advertisement.model.*
import com.bonial.codechallenge.ui.ViewState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description
import app.cash.turbine.test
import app.cash.turbine.ReceiveTurbine
import app.cash.turbine.Turbine
import app.cash.turbine.awaitItem
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.just
import kotlinx.coroutines.test.advanceUntilIdle


class BrochureViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule() // custom rule for Dispatchers.Main

    private val advertisementRepository: AdvertisementRepository = mockk(relaxed = true)

    private lateinit var viewModel: BrochureViewModel

    @Before
    fun setup() {
        // Default flow is empty
        every { advertisementRepository.advertisementsFlow } returns MutableStateFlow(null)

        viewModel = BrochureViewModel(advertisementRepository)
    }

    @Test
    fun `GIVEN the view model, WHEN flow from repository has error, THEN viewState should emit Failure`() = runTest {

        val fakeFlow = MutableStateFlow<Result<List<ContentItem>>?>(null)
        val fakeRepo = mockk<AdvertisementRepository>(relaxed = true) {
            every { advertisementsFlow } returns fakeFlow
            coEvery { getAllAdvertisementItems(any(), any()) } just Runs
        }

        val viewModel = BrochureViewModel(fakeRepo)

        viewModel.viewState.test {
            assert(awaitItem() is ViewState.Loading)

            val exception = RuntimeException("Network error")
            fakeFlow.emit(Result.failure(exception))

            assert(awaitItem() is ViewState.Failure)
            cancelAndIgnoreRemainingEvents()
        }
    }


    @Test
    fun `GIVEN the view model, WHEN flow from repository has data, THEN viewState should emit Success`() = runTest {

        val ads = listOf(ContentItem(
                contentType = ContentType.BROCHURE,
                content = ContentVariant.Brochure(
                    id = 1,
                    publisher = Publisher(name = "Test Store"),
                    distance = 1.2,
                    brochureImage = "url"
                )
            )
        )

        val flow = MutableStateFlow<Result<List<ContentItem>>?>(Result.success(ads))
        val fakeRepo = mockk<AdvertisementRepository>(relaxed = true) {
            every { advertisementsFlow } returns flow
            coEvery { getAllAdvertisementItems(any(), any()) } just Runs
        }

        viewModel = BrochureViewModel(fakeRepo)

        viewModel.viewState.test {

            val success = awaitItem()
            assertTrue(success is ViewState.Success)
            val uiModels = (success as ViewState.Success).data
            assertEquals(1, uiModels.size)
            assertEquals("Test Store", uiModels[0].retailerName)
        }
    }

    @Test
    fun `GIVEN the view model, WHEN giving Double number to formatDistance method, THEN should returned in human readable manner`() {
        var result = viewModel.formatDistance(1.234344566)
        assertEquals("1,23 km", result)

        result = viewModel.formatDistance(0.567)
        assertEquals("567 m", result)
    }

    @Test
    fun `GIVEN the view model, WHEN calling the formatExpiry method, THEN should return expiry in human readable manner`() {
        val result = viewModel.formatExpiry()

        assertTrue(result.startsWith("Expires in "))
        assertTrue(result.endsWith(" days"))

        val days = result.removePrefix("Expires in ").removeSuffix(" days").toInt()
        assertTrue(days in 1..29)
    }
}
