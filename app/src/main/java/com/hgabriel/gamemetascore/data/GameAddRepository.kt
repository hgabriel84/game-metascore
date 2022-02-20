package com.hgabriel.gamemetascore.data

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GameAddRepository @Inject constructor(
    private val igdbDataSource: IgdbDataSource,
    private val gamesRepository: GamesRepository
) {

    suspend fun getGameDetail(id: Int): Resource<IgdbGame> = igdbDataSource.getGameDetail(id)

    suspend fun searchGame(keyword: String): Resource<List<IgdbGame>> =
        igdbDataSource.searchGame(keyword)

    suspend fun addGame(igdbGame: IgdbGame) {
        gamesRepository.addGame(igdbGame.toGame())
    }

    private fun IgdbGame.toGame() = Game(
        id = id,
        name = name,
        cover = cover,
        summary = summary,
        storyline = storyline,
        criticsRating = criticsRating,
        usersRating = usersRating,
        totalRating = totalRating
    )
}
