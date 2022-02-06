package com.hgabriel.gamemetascore.utilities

import android.content.Context
import androidx.core.content.ContextCompat
import com.hgabriel.gamemetascore.R
import kotlin.math.roundToInt

fun Float?.toLabel(): String = this?.roundToInt()?.toString() ?: "-"

fun getTotalRatingTextColor(context: Context, score: Float?) =
    score?.let {
        when (it) {
            in 0f..49.9f -> ContextCompat.getColor(context, R.color.red_score)
            in 50f..79.9f -> ContextCompat.getColor(context, R.color.yellow_score)
            in 80f..100f -> ContextCompat.getColor(context, R.color.green_score)
            else -> ContextCompat.getColor(context, R.color.primary)
        }
    } ?: ContextCompat.getColor(context, R.color.primary)
