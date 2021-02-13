package com.hgabriel.videogames.scores.data.vo

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "game",
    indices = [Index("gameUrl", unique = true)]
)
data class Game(
    val name: String,
    @PrimaryKey
    val imageUrl: String,
    val metascore: Int,
    val userScore: Float,
    val averageScore: Float,
    val played: Boolean
)