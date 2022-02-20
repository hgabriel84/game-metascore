package com.hgabriel.gamemetascore.data

data class IgdbGame(
    val id: Int,
    val name: String,
    val coverId: Int? = null,
    var cover: String? = null,
    val storyline: String? = null,
    val summary: String? = null,
    val criticsRating: Float? = null,
    val usersRating: Float? = null,
    val totalRating: Float? = null
)
