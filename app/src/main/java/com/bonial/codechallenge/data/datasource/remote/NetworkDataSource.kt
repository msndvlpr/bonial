package com.bonial.codechallenge.data.datasource.remote

import com.bonial.codechallenge.data.repositpry.advertisement.model.NetworkResponse

import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.http.HttpHeaders
import io.ktor.http.headers
import kotlinx.serialization.json.Json
import timber.log.Timber

internal class NetworkDataSource(private val clientProvider: suspend () -> HttpClient) {

    suspend fun getAllAdvertisements(): Result<NetworkResponse> =

        runCatching<NetworkDataSource, NetworkResponse> {
            val response = clientProvider().get("https://3vq81.wiremockapi.cloud/advertisements") {
            //todo val response = clientProvider().get("${GET_ADS_URL}shelf.json") {
                headers {
                    append(HttpHeaders.ContentType, "application/json")
                }
            }
            val jsonString = response.bodyAsText()
            Timber.d("msn Raw response: $jsonString")

            Json.decodeFromString<NetworkResponse>(jsonString)

        }.onFailure { error ->
            println("msn error: $error")
        }

}
