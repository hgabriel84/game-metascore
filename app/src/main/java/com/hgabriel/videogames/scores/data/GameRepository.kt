package com.hgabriel.videogames.scores.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(private val gameDao: GameDao) {

    fun getGames(order: GameOrder) =
        when (order) {
            GameOrder.RATING -> gameDao.getGamesByRating()
            GameOrder.NAME -> gameDao.getGamesByName()
        }

    fun addGame(game: Game) = gameDao.insert(game)

    fun removeGame(game: Game) = gameDao.delete(game)

}
