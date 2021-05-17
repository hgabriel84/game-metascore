package com.hgabriel.videogames.scores.data.vo

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Game(
    val name: String,
    @PrimaryKey
    val gamePath: String,
    val imageUrl: String,
    val metascore: Int?,
    val userScore: Float?,
    val averageScore: Float?,
    var played: Boolean,
    var liked: Boolean
)