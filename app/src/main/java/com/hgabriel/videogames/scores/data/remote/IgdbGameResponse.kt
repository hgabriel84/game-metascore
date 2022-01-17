package com.hgabriel.videogames.scores.data.remote

import com.squareup.moshi.Json

data class IgdbGameResponse(
    val id: Int,
    val name: String,
    val cover: Int?,
    val description: String?,
    @Json(name = "aggregated_rating") val criticsRating: Float?,
    @Json(name = "rating") val usersRating: Float?,
    @Json(name = "total_rating") val totalRating: Float?
)

data class CoverResponse(
    @Json(name = "image_id") val imageId: String
)
