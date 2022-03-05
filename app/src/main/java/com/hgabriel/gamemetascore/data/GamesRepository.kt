package com.hgabriel.gamemetascore.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GamesRepository @Inject constructor(private val gamesDao: GamesDao) {

    fun games(order: GameOrder) =
        when (order) {
            GameOrder.RATING -> gamesDao.gamesByRating()
            GameOrder.NAME -> gamesDao.gamesByName()
        }

    fun games(order: GameOrder, keyword: String) =
        when (order) {
            GameOrder.RATING -> gamesDao.gamesByRating("%${keyword.uppercase()}%")
            GameOrder.NAME -> gamesDao.gamesByName("%${keyword.uppercase()}%")
        }

    suspend fun getGame(id: Int) = gamesDao.getGameById(id)

    suspend fun addGame(game: Game) = gamesDao.insert(game)

    suspend fun removeGame(id: Int) = gamesDao.deleteById(id)

    suspend fun togglePlayed(id: Int) {
        val game = gamesDao.getGameById(id).apply { played = !played }
        gamesDao.insert(game)
    }

    suspend fun toggleLiked(id: Int) {
        val game = gamesDao.getGameById(id).apply { liked = !liked }
        gamesDao.insert(game)
    }

    suspend fun getGames() = gamesDao.getGames()
}
