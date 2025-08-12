package com.bonial.codechallenge.data.repositpry.advertisement

import com.bonial.codechallenge.data.repositpry.advertisement.model.CarouselContent
import com.bonial.codechallenge.data.repositpry.advertisement.model.ContentVariant
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.*

object ContentVariantSerializer : KSerializer<ContentVariant> {
    override val descriptor: SerialDescriptor =
        buildClassSerialDescriptor("ContentVariant")

    override fun deserialize(decoder: Decoder): ContentVariant {
        val jsonElement = decoder as? JsonDecoder
            ?: error("This serializer can only be used with Json")

        val element = jsonElement.decodeJsonElement()

        return when (element) {
            is JsonArray -> {
                val items = element.map { Json.decodeFromJsonElement(CarouselContent.serializer(), it) }
                ContentVariant.SuperBannerList(items)
            }
            is JsonObject -> {
                val brochure = Json.decodeFromJsonElement(ContentVariant.Brochure.serializer(), element)
                brochure
            }
            else -> error("Unknown content type: $element")
        }
    }

    override fun serialize(encoder: Encoder, value: ContentVariant) {
        when (value) {
            is ContentVariant.SuperBannerList -> encoder.encodeSerializableValue(
                ListSerializer(CarouselContent.serializer()),
                value.items
            )
            is ContentVariant.Brochure -> encoder.encodeSerializableValue(
                ContentVariant.Brochure.serializer(),
                value
            )
        }
    }
}
