package com.hgabriel.videogames.scores.data.remote

import com.hgabriel.videogames.scores.data.vo.Game
import org.jsoup.Jsoup
import timber.log.Timber

fun String.toGame(pathName: String): Game? {
    try {
        val doc = Jsoup.parse(this)

        val name = doc.select("div.product_title").select("h1").text()
        val imageUrl =
            doc.select("div.product_media").select("img.product_image").first().attr("src")
        val scores = doc.select("div.product_scores")
        val metascore =
            scores.select("div.metascore_wrap").select("div.metascore_w").first().text().toInt()
        val userScore =
            scores.select("div.userscore_wrap").select("div.metascore_w").first().text().toFloat()

        return Game(
            name = name,
            pathName = pathName,
            imageUrl = imageUrl,
            metascore = metascore,
            userScore = userScore,
            averageScore = (metascore + (userScore * 10)) / 2,
            played = false
        )
    } catch (t: Throwable) {
        Timber.d(t, "Error scraping game info from html.")
        return null
    }
}