package com.hgabriel.gamemetascore.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MetaGameRepository @Inject constructor(
    private val igdbDataSource: IgdbDataSource,
    private val gamesRepository: GamesRepository
) {

    suspend fun getGameDetail(id: Int): Resource<IgdbGame> = igdbDataSource.getGameDetail(id)

    suspend fun addGame(game: Game) {
        gamesRepository.addGame(game)
    }

    suspend fun getGames() = gamesRepository.getGames()

}
