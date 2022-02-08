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

    suspend fun getGame(id: Int) = gameDao.getGameById(id)

    suspend fun addGame(game: Game) = gameDao.insert(game)

    suspend fun removeGame(game: Game) = gameDao.delete(game)

    suspend fun togglePlayed(id: Int) {
        val game = gameDao.getGameById(id).apply { played = !played }
        gameDao.insert(game)
    }

    suspend fun toggleLiked(id: Int) {
        val game = gameDao.getGameById(id).apply { liked = !liked }
        gameDao.insert(game)
    }

}
