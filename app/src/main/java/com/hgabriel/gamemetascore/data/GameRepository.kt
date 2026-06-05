package com.hgabriel.gamemetascore.data

import com.hgabriel.gamemetascore.utilities.toGame
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameRepository @Inject constructor(
    private val igdbDataSource: IgdbDataSource,
    private val gameDao: GameDao
) {

    fun games(order: GameOrder) =
        when (order) {
            GameOrder.RATING -> gameDao.gamesByRating()
            GameOrder.NAME -> gameDao.gamesByName()
        }

    fun games(order: GameOrder, keyword: String) =
        when (order) {
            GameOrder.RATING -> gameDao.gamesByRating("%${keyword.uppercase()}%")
            GameOrder.NAME -> gameDao.gamesByName("%${keyword.uppercase()}%")
        }

    suspend fun games() = gameDao.games()

    suspend fun game(id: Int) = gameDao.gameById(id)

    suspend fun addGame(game: Game) = gameDao.insert(game)

    suspend fun removeGame(id: Int) = gameDao.deleteById(id)

    suspend fun togglePlayed(id: Int) {
        val game = gameDao.gameById(id).apply { played = !played }
        gameDao.insert(game)
    }

    suspend fun toggleLiked(id: Int) {
        val game = gameDao.gameById(id).apply { liked = !liked }
        gameDao.insert(game)
    }

    suspend fun searchGame(keyword: String) = igdbDataSource.searchGame(keyword)

    suspend fun gamesDetail(ids: List<Int>) = igdbDataSource.gamesDetail(ids)

    suspend fun sync() {
        val games = gameDao.games()
        games.chunked(50).forEach { chunk ->
            val ids = chunk.map { it.id }
            val resource = igdbDataSource.gamesDetail(ids)
            if (resource.status == Resource.Status.SUCCESS) {
                resource.data!!.forEach { igdbGame ->
                    val localGame = chunk.first { it.id == igdbGame.id }
                    val game = igdbGame.toGame().apply {
                        liked = localGame.liked
                        played = localGame.played
                    }
                    if (localGame != game) {
                        gameDao.insert(game)
                    }
                }
            }
        }
    }
}
