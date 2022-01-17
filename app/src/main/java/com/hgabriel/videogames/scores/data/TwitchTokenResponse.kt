package com.hgabriel.videogames.scores.data

import com.squareup.moshi.Json

data class TwitchTokenResponse(
    @Json(name = "access_token") val accessToken: String,
    @Json(name = "expires_in") val expiresIn: String,
    @Json(name = "token_type") val tokenType: String
)
