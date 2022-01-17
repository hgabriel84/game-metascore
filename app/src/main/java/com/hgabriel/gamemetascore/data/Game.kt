package com.hgabriel.gamemetascore.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @PrimaryKey val id: Int,
    val name: String,
    val coverId: Int? = null,
    var cover: String? = null,
    val criticsRating: Float? = null,
    val usersRating: Float? = null,
    val totalRating: Float? = null,
    var played: Boolean = false,
    var liked: Boolean = false
)

enum class GameOrder { RATING, NAME }
