package com.bonial.codechallenge.data.repositpry.advertisement.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class ClosestStore(

    val id: String? = null,

    val latitude: Double? = null,

    val longitude: Double? = null,

    val street: String? = null,

    val streetNumber: String? = null,

    val zip: String? = null,

    val city: String? = null

)
