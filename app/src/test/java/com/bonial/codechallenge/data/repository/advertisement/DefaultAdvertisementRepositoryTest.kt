package com.bonial.codechallenge.data.repository.advertisement

import com.bonial.codechallenge.data.datasource.remote.NetworkDataSource
import com.bonial.codechallenge.data.repositpry.advertisement.DefaultAdvertisementRepository
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentItem
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentType
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentVariant
import com.bonial.codechallenge.data.repositpry.advertisement.model.Embedded
import com.bonial.codechallenge.data.repositpry.advertisement.model.NetworkResponse
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import kotlin.test.Test


@OptIn(ExperimentalCoroutinesApi::class)
class DefaultAdvertisementRepositoryTest {

    private val networkDataSource: NetworkDataSource = mockk()
    private lateinit var repository: DefaultAdvertisementRepository

    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        repository = DefaultAdvertisementRepository(networkDataSource)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `GIVEN AdvertisementRepository WHEN getAllAdvertisementItems is called with success THEN emits filtered list`() = runTest {

        val brochure1 = ContentItem(
            contentType = ContentType.BROCHURE,
            content = ContentVariant.Brochure(distance = 4.0)
        )
        val brochure2 = ContentItem(
            contentType = ContentType.BROCHURE_PREMIUM,
            content = ContentVariant.Brochure(distance = 6.0) // should be filtered out
        )
        val networkResponse = NetworkResponse(
            embedded = Embedded(contentItems = listOf(brochure1, brochure2))
        )

        coEvery { networkDataSource.getAllAdvertisements() } returns Result.success(networkResponse)

        val collected = mutableListOf<Result<List<ContentItem>?>?>()

        val job = launch(testDispatcher) {
            repository.advertisementsFlow.toList(collected)
        }

        repository.getAllAdvertisementItems(
            contentType = listOf(ContentType.BROCHURE),
            distance = 5.0
        )

        advanceUntilIdle()
        val result = collected.last()?.getOrNull()
        assertEquals(listOf(brochure1), result)

        job.cancel()
    }

    @Test
    fun `GIVEN AdvertisementRepository WHEN getAllAdvertisementItems is called with failure THEN emits exception`() = runTest {

        val exception = RuntimeException("Network error")
        coEvery { networkDataSource.getAllAdvertisements() } returns Result.failure(exception)

        val collected = mutableListOf<Result<List<ContentItem>?>?>()

        val job = launch(testDispatcher) {
            repository.advertisementsFlow.toList(collected)
        }

        repository.getAllAdvertisementItems(
            contentType = listOf(ContentType.BROCHURE),
            distance = 5.0
        )

        advanceUntilIdle()
        val result = collected.last()
        assertTrue(result?.isFailure == true)
        assertEquals(exception, result?.exceptionOrNull())

        job.cancel()
    }
}