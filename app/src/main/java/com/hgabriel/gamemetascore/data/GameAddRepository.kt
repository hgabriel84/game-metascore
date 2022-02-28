package com.hgabriel.gamemetascore.data

import com.hgabriel.gamemetascore.utilities.toGame
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameAddRepository @Inject constructor(
    private val igdbDataSource: IgdbDataSource,
    private val gamesRepository: GamesRepository
) {

    suspend fun searchGame(keyword: String): Resource<List<IgdbGame>> =
        igdbDataSource.searchGame(keyword)

    suspend fun addGame(igdbGame: IgdbGame) {
        gamesRepository.addGame(igdbGame.toGame())
    }
}
