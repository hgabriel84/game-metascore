package com.hgabriel.gamemetascore.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(private val gameDao: GameDao) {

    fun games(order: GameOrder) =
        when (order) {
            GameOrder.RATING -> gameDao.gamesByRating()
            GameOrder.NAME -> gameDao.gamesByName()
        }

    suspend fun getGame(id: Int) = gameDao.getGameById(id)

    suspend fun addGame(game: Game) = gameDao.insert(game)

    suspend fun removeGame(id: Int) = gameDao.deleteById(id)

    suspend fun togglePlayed(id: Int) {
        val game = gameDao.getGameById(id).apply { played = !played }
        gameDao.insert(game)
    }

    suspend fun toggleLiked(id: Int) {
        val game = gameDao.getGameById(id).apply { liked = !liked }
        gameDao.insert(game)
    }

}
