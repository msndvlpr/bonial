package com.bonial.codechallenge.ui.home.widget

import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasAnySibling
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentType
import com.bonial.codechallenge.ui.model.BrochureUiModel
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
class BrochureGridTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val brochures = listOf(
        BrochureUiModel(
            id = 1,
            retailerName = "Retailer 1",
            imageUrl = "https://example.com/image1.jpg",
            isFavourite = false,
            type = ContentType.BROCHURE,
            formattedExpiry = "Expires in 2 days",
            formattedDistance = "1.2 km"
        ),
        BrochureUiModel(
            id = 2,
            retailerName = "Premium Retailer",
            imageUrl = "https://example.com/image2.jpg",
            isFavourite = true,
            type = ContentType.BROCHURE_PREMIUM,
            formattedExpiry = "Expires today",
            formattedDistance = "0.5 km"
        ),
        BrochureUiModel(
            id = 3,
            retailerName = "Retailer 3",
            imageUrl = "", // force error state
            isFavourite = false,
            type = ContentType.BROCHURE,
            formattedExpiry = "Expires tomorrow",
            formattedDistance = "3.0 km"
        )
    )

    @Test
    fun `GIVEN the widget in home screen, WHEN data is available, THEN it should show brochures with their names on screen`() {
        composeTestRule.setContent {
            MaterialTheme {
                BrochureGrid(brochures = brochures, columns = 2)
            }
        }
        brochures.forEach {
            composeTestRule.onNodeWithText(it.retailerName).assertIsDisplayed()
        }
    }

    @Test
    fun `GIVEN the widget in home screen, WHEN clicked on item's like icon, THEN it should be changed`() {
        composeTestRule.setContent {
            MaterialTheme {
                BrochureGrid(brochures = brochures, columns = 2)
            }
        }
        val targetRetailer = brochures.first().retailerName

        composeTestRule.onNode(
            hasContentDescription("Favourite")
                .and(hasAnySibling(hasText(targetRetailer)))
        ).performClick()

        composeTestRule.onNodeWithTag("tag_liked").assertExists()
    }

    @Test
    fun `GIVEN the widget in home screen, WHEN image url is invalid, THEN it should show the placeholder on screen`() {
        composeTestRule.setContent {
            MaterialTheme {
                BrochureGrid(brochures = brochures, columns = 2)
            }
        }
        composeTestRule.onNodeWithText("No image available").assertIsDisplayed()
    }

    @Test
    fun `GIVEN the widget in home screen, WHEN data is available, THEN it should show the retailer name on screen`() {
        composeTestRule.setContent {
            MaterialTheme {
                BrochureGrid(brochures = brochures, columns = 2)
            }
        }
        composeTestRule.onNodeWithText("Retailer 1").assertIsDisplayed()
    }
}