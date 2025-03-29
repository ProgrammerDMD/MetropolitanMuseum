package me.programmerdmd.metropolitanmuseum.objects.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class MuseumObject(
    @Json(name = "objectID") val id: Int,
    @Json(name = "title") val title: String,
    @Json(name = "artistDisplayName") val artist: String,
    @Json(name = "primaryImageSmall") val image: String,
    @Json(name = "additionalImages") val additionalImages: List<String>,
    @Json(name = "objectDate") val date: String,
    @Json(name = "department") val department: String,
    @Json(name = "country") val country: String,
    @Json(name = "state") val state: String,
    @Json(name = "medium") val medium: String
)
