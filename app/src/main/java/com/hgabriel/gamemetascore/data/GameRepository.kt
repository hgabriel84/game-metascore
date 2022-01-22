package com.hgabriel.gamemetascore.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(private val gameDao: GameDao) {

    suspend fun getGames(order: GameOrder) =
        when (order) {
            GameOrder.RATING -> gameDao.getGamesByRating()
            GameOrder.NAME -> gameDao.getGamesByName()
        }

    suspend fun addGame(game: Game) = gameDao.insert(game)

    suspend fun removeGame(game: Game) = gameDao.delete(game)

}
