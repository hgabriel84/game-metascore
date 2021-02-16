package com.hgabriel.videogames.scores.data.remote

import com.hgabriel.videogames.scores.data.vo.Game
import org.jsoup.Jsoup
import timber.log.Timber

fun String.toGame(gamePath: String): Game? {
    try {
        val doc = Jsoup.parse(this)

        val name = doc.select("div.product_title").select("h1").text()
        val imageUrl =
            doc.select("div.product_media").select("img.product_image").first().attr("src")
        val scores = doc.select("div.product_scores")
        val metascore = try {
            scores.select("div.metascore_wrap").select("div.metascore_w").first().text().toInt()
        } catch (t: Throwable) {
            Timber.d(t, "Not enough critic reviews to calculate metascore.")
            null
        }
        val userScore = try {
            scores.select("div.userscore_wrap").select("div.metascore_w").first().text().toFloat()
        } catch (t: Throwable) {
            Timber.d(t, "Not enough user reviews to calculate user score.")
            null
        }

        val averageScore = if (userScore != null && metascore != null) {
            (metascore + (userScore * 10)) / 2
        } else {
            null
        }

        return Game(
            name = name,
            gamePath = gamePath,
            imageUrl = imageUrl,
            metascore = metascore,
            userScore = userScore,
            averageScore = averageScore,
            played = false
        )
    } catch (t: Throwable) {
        Timber.d(t, "Error scraping game data from html.")
        return null
    }
}