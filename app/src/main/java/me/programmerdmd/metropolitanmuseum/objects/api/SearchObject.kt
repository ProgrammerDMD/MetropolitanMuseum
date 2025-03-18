package me.programmerdmd.metropolitanmuseum.objects.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SearchObject(
    @Json(name = "total") val total: Int,
    @Json(name = "objectIDs") val objectIDs: List<Int>?
)
