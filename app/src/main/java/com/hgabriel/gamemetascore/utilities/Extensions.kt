package com.hgabriel.gamemetascore.utilities

import android.content.Context
import androidx.core.content.ContextCompat
import com.hgabriel.gamemetascore.R
import com.hgabriel.gamemetascore.data.Game
import com.hgabriel.gamemetascore.data.IgdbGame
import com.hgabriel.gamemetascore.data.MetaGame
import kotlin.math.roundToInt

fun Float?.toLabel(): String = this?.roundToInt()?.toString() ?: "-"

fun getTotalRatingTextColor(context: Context, score: Float?) =
    score?.let {
        when (it) {
            in 0f..50f -> ContextCompat.getColor(context, R.color.red_score)
            in 50f..80f -> ContextCompat.getColor(context, R.color.yellow_score)
            in 80f..100f -> ContextCompat.getColor(context, R.color.green_score)
            else -> ContextCompat.getColor(context, R.color.yellow_score)
        }
    } ?: ContextCompat.getColor(context, R.color.yellow_score)

fun IgdbGame.toGame() = Game(
    id = id,
    name = name,
    cover = cover,
    summary = summary,
    storyline = storyline,
    criticsRating = criticsRating,
    usersRating = usersRating,
    totalRating = totalRating
)

fun Game.toMetaGame() = MetaGame(
    id = id,
    name = name,
    played = played,
    liked = liked
)
