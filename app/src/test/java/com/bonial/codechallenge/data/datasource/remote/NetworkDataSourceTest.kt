package com.bonial.codechallenge.data.datasource.remote

import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentItem
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentType
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentVariant
import com.bonial.codechallenge.data.repositpry.advertisement.model.Embedded
import com.bonial.codechallenge.data.repositpry.advertisement.model.NetworkResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNotNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test


class NetworkDataSourceTest {

    private lateinit var dataSource: NetworkDataSource

    @Test
    fun `GIVEN NetworkDataSource WHEN getAllAdvertisements is called and json response is valid THEN it returns success`() = runTest {

        val expectedResponse = NetworkResponse(
            embedded = Embedded(
                contentItems = listOf(
                    ContentItem(
                        placement = "placement",
                        contentType = ContentType.BROCHURE,
                        content = ContentVariant.Brochure(id = 0, title = "title")
                    )
                )
            )
        )
        val jsonString = Json.encodeToString(expectedResponse)

        val mockEngine = MockEngine { _ ->
            respond(
                content = jsonString,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val clientProvider: suspend () -> HttpClient = { HttpClient(mockEngine) }

        dataSource = NetworkDataSource(clientProvider)

        val result = dataSource.getAllAdvertisements()

        assertTrue(result.isSuccess)
        assertEquals(expectedResponse, result.getOrNull())
    }

    @Test
    fun `GIVEN NetworkDataSource WHEN getAllAdvertisements is called and json response is invalid THEN it returns failure`() = runTest {

        val invalidJson = "{ invalid json }"

        val mockEngine = MockEngine { _ ->
            respond(
                content = invalidJson,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }

        val clientProvider: suspend () -> HttpClient = { HttpClient(mockEngine) }

        dataSource = NetworkDataSource(clientProvider)

        val result = dataSource.getAllAdvertisements()

        assertTrue(result.isFailure)
        assertNotNull(result.exceptionOrNull())
    }
}
