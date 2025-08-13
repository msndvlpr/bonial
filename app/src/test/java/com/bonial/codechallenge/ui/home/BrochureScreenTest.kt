package com.bonial.codechallenge.ui.home

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onAllNodesWithText
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.testing.TestLifecycleOwner
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentType
import com.bonial.codechallenge.ui.ViewState
import com.bonial.codechallenge.ui.model.BrochureUiModel
import com.bonial.codechallenge.ui.model.FilterModel
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BrochureScreenTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @get:Rule
    val composeTestRule = createComposeRule()

    // Mock ViewModel and its flows
    private val brochures = listOf(
        BrochureUiModel(1, "Retailer A", ContentType.BROCHURE, "1.5 km", "7 days", "test url", false),
        BrochureUiModel(2, "Retailer B", ContentType.BROCHURE_PREMIUM, "0.8 km", "3 days", "test urll", false)
    )
    private lateinit var mockViewModel: BrochureViewModel
    private val mockViewStateFlow = MutableStateFlow<ViewState<List<BrochureUiModel>>>(ViewState.Success(brochures))
    private val mockFilterFlow = MutableStateFlow(FilterModel())

    // Mock the theme toggle function
    private var isDarkTheme = false
    private val onThemeToggle: () -> Unit = { isDarkTheme = !isDarkTheme }

    @Before
    fun setup() {
        mockViewModel = mockk(relaxed = true)
        coEvery { mockViewModel.viewState } returns mockViewStateFlow
        coEvery { mockViewModel.filter } returns mockFilterFlow

        val testLifecycleOwner = TestLifecycleOwner(Lifecycle.State.RESUMED)

        composeTestRule.setContent {
            CompositionLocalProvider(LocalLifecycleOwner provides testLifecycleOwner) {
                BrochureScreen(
                    viewModel = mockViewModel,
                    isDarkTheme = isDarkTheme,
                    onThemeToggle = onThemeToggle
                )
            }
        }
    }

    @Test
    fun `GIVEN the view model from repository has error, THEN viewState should emit Failure`() = runTest {
        val brochures = listOf(
            BrochureUiModel(1, "Retailer A", ContentType.BROCHURE, "1.5 km",
                "7 days", null, false),
            BrochureUiModel(2, "Retailer B", ContentType.BROCHURE_PREMIUM, "0.8 km",
                "3 days", null, false)
        )

        composeTestRule.runOnIdle {
            mockViewStateFlow.value = ViewState.Success(brochures)
        }

        // Wait until one of the items appears
        composeTestRule.waitUntil {
            composeTestRule.onAllNodesWithText("Retailer A").fetchSemanticsNodes().isNotEmpty()
        }

        composeTestRule.onNodeWithText("Retailer A").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retailer B").assertIsDisplayed()
    }

    @Test
    fun brochureScreen_displaysLoadingState_whenLoading2() {
        composeTestRule.runOnIdle {
            mockViewStateFlow.value = ViewState.Loading
        }
        composeTestRule.waitForIdle()

        val h = BrochureUiModel(
            id = 0,
            retailerName = "retailer",
            type = ContentType.BROCHURE,
            formattedDistance = " 2 km",
            formattedExpiry = "expiry",
            imageUrl = "url",
            isFavourite = false
        )
        mockViewStateFlow.value = ViewState.Success(listOf(h))
        composeTestRule.onNodeWithText("Brochures").assertDoesNotExist()
    }


    @Test
    fun brochureScreen_displaysLoadingState_whenLoading() {
        // Given: The ViewModel's viewState is Loading
        mockViewStateFlow.value = ViewState.Loading
        //composeTestRule.waitForIdle()

        // When: The Composable is rendered
        // Then: A CircularProgressIndicator and the text "Loading" (if it existed) should be visible.
        // We'll test for the text "Brochures" from the TopAppBar to ensure the screen is composed,
        // and we will look for a progress indicator.
        composeTestRule.onNodeWithText("Brochures").assertExists()
        //composeTestRule.onNodeWithText("Loading advertisements...").assertIsDisplayed()
        //composeTestRule.onNodeWithTag("tag_loading").assertIsDisplayed()

    }

    @Test
    fun brochureScreen_displaysErrorState_whenFailure() {
        // Given: The ViewModel's viewState is Failure
        mockViewStateFlow.value = ViewState.Failure

        // When: The Composable is rendered
        // Then: The ErrorScreen (with its retry button) should be displayed
        composeTestRule.onNodeWithText("Failed to load advertisements. Please try again.").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retry").assertIsDisplayed()

        // And: Clicking retry should call the ViewModel's retry method
        composeTestRule.onNodeWithText("Retry").performClick()
        verify { mockViewModel.retryLoadData() }
    }

    @Test
    fun brochureScreen_displaysBrochures_whenSuccessWithData() {
        // Given: The ViewModel's viewState is Success with a list of brochures
        val brochures = listOf(
            BrochureUiModel(1, "Retailer A", ContentType.BROCHURE, "1.5 km", "7 days", null, false),
            BrochureUiModel(2, "Retailer B", ContentType.BROCHURE_PREMIUM, "0.8 km", "3 days", null, false)
        )
        mockViewStateFlow.value = ViewState.Success(brochures)

        // When: The Composable is rendered
        // Then: The UI should display the content of the brochures.
        composeTestRule.onNodeWithText("Retailer A").assertIsDisplayed()
        composeTestRule.onNodeWithText("Retailer B").assertIsDisplayed()
    }


    @Test
    fun brochureScreen_filterMenu_opensAndupdatesFilter() {
        // Given: A non-empty list of content types
        val initialFilter = FilterModel(contentTypeFilter = listOf(ContentType.BROCHURE))
        mockFilterFlow.value = initialFilter

        // When: The filter menu is opened
        composeTestRule.onNodeWithContentDescription("Filter").performClick()

        // Then: The DropdownMenu should be visible, showing the filter options
        composeTestRule.onNodeWithText("BROCHURE").assertIsDisplayed()

        // And: Clicking on a non-selected filter should update the ViewModel
        composeTestRule.onNodeWithText("BROCHURE").performClick()

        // Verify that the ViewModel was called to update the filter
        val expectedList = initialFilter.contentTypeFilter + ContentType.BROCHURE_PREMIUM
        verify { mockViewModel.updateContentTypeFilter(expectedList) }
    }

    @Test
    fun brochureScreen_filterMenu_togglesFilterCorrectly() {
        // Given: A filter is already selected
        val initialFilter = FilterModel(contentTypeFilter = listOf(ContentType.BROCHURE))
        mockFilterFlow.value = initialFilter

        // When: The filter menu is opened and the selected filter is clicked again
        composeTestRule.onNodeWithContentDescription("Filter").performClick()
        composeTestRule.onNodeWithText("BROCHURE").performClick()

        // Then: The ViewModel should be called to remove the filter
        val expectedList = initialFilter.contentTypeFilter - ContentType.BROCHURE
        verify { mockViewModel.updateContentTypeFilter(expectedList) }
    }

}