package com.hgabriel.gamemetascore.scores.data.vo

data class MetaGame(
    val id: Int,
    val name: String,
    val played: Boolean,
    val liked: Boolean
)

data class MetaGames(
    val games: List<MetaGame>
)
