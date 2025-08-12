package com.bonial.codechallenge.di

import com.bonial.codechallenge.data.datasource.remote.NetworkDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import com.bonial.codechallenge.data.repositpry.advertisement.AdvertisementRepository
import com.bonial.codechallenge.data.repositpry.advertisement.DefaultAdvertisementRepository
import io.ktor.client.HttpClient
import io.ktor.client.engine.android.Android
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logging
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import timber.log.Timber
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class AdsModule {

    @Provides
    @Singleton
    internal fun provideNetworkDataSource(httpClient: HttpClient): NetworkDataSource {
        return NetworkDataSource { httpClient }
    }

    @Provides
    @Singleton
    fun provideKtorClient(): HttpClient {
        val client = HttpClient(Android) {
            expectSuccess = true

            install(ContentNegotiation) {
                json(Json {
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
            install(HttpTimeout) {
                requestTimeoutMillis = 5000 // Max time for the entire request
                connectTimeoutMillis = 5000 // Timeout for establishing a connection
                socketTimeoutMillis = 5000 // Timeout for socket read/write
            }
            install(Logging) {
                logger = object : io.ktor.client.plugins.logging.Logger {
                    override fun log(message: String) {
                        Timber.d(message)
                    }
                }
                level = LogLevel.ALL
            }

        }
        return client
    }


    @Provides
    @Singleton
    internal fun provideAdvertisementRepository(
        networkDataSource: NetworkDataSource
    ): AdvertisementRepository = DefaultAdvertisementRepository(networkDataSource)

}