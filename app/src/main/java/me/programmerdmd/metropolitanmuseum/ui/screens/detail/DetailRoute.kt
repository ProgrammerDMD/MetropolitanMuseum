package me.programmerdmd.metropolitanmuseum.ui.screens.detail

import kotlinx.serialization.Serializable

@Serializable
data class DetailRoute(
    val objectId: Int,
    val title: String
)