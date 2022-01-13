package com.hgabriel.videogames.scores.data.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    @PrimaryKey val id: Int,
    val name: String,
    val coverId: Int,
    var cover: String? = null,
    val criticsRating: Float?,
    val usersRating: Float?,
    val totalRating: Float?,
    var played: Boolean = false,
    var liked: Boolean = false
)
