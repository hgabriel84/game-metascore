package com.hgabriel.gamemetascore.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetaGameRepository @Inject constructor(
    private val igdbDataSource: IgdbDataSource,
    private val gamesDao: GamesDao
) {

    suspend fun getGameDetail(id: Int): Resource<IgdbGame> = igdbDataSource.getGameDetail(id)

    suspend fun addGame(game: Game) {
        gamesDao.insert(game)
    }

    suspend fun getGames() = gamesDao.getGames()
}
