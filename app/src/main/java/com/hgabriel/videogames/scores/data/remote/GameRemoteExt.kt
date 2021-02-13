package com.hgabriel.videogames.scores.data.remote

import com.hgabriel.videogames.scores.data.vo.Game
import org.jsoup.Jsoup

fun String.toGame(): Game {
    val doc = Jsoup.parse(this)
    return Game(
        name = "the-last-of-us-remastered",
        gameUrl = "https://www.metacritic.com/game/playstation-4/the-last-of-us-remastered",
        imageUrl = "https://static.metacritic.com/images/products/games/6/dcb5d2f7d2f23d0d4aa8fd809af6b12e-98.jpg",
        metascore = 95,
        userScore = 9.2f,
        averageScore = 95 / 9.2f,
        played = true
    )
}