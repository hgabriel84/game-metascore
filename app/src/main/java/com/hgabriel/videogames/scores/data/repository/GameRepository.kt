package com.hgabriel.videogames.scores.data.repository

import com.hgabriel.videogames.scores.data.local.GameDao
import com.hgabriel.videogames.scores.data.remote.GameRemoteDataSource
import com.hgabriel.videogames.scores.data.vo.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GameRepository @Inject constructor(
    private val gameRemoteDataSource: GameRemoteDataSource,
    private val gameDao: GameDao
) {

    suspend fun fetchGames(order: GameOrder): Flow<Resource<GamesResponse>> =
        flow {
            emit(Resource.loading(null))
            val dbResult = loadFromDb(order)
            emit(dbResult)
            dbResult.data?.result?.map { it.id }?.let { ids ->
                gameRemoteDataSource.getGames(ids).data?.forEach { remoteGame ->
                    val dbGame = dbResult.data.result.find { it.id == remoteGame.id }
                    remoteGame.apply {
                        played = dbGame?.played ?: false
                        liked = dbGame?.liked ?: false
                    }
                }
            }
        }.flowOn(Dispatchers.IO)

    suspend fun loadGamesFromDb(order: GameOrder): Flow<Resource<GamesResponse>> =
        flow {
            val dbResult = loadFromDb(order)
            emit(dbResult)
        }.flowOn(Dispatchers.IO)

    suspend fun addGame(game: Game, order: GameOrder): Flow<Resource<GamesResponse>> =
        flow {
            emit(Resource.loading(null))
            gameDao.insert(game)
            emit(loadFromDb(order))
        }.flowOn(Dispatchers.IO)

    suspend fun deleteGame(game: Game, order: GameOrder): Flow<Resource<GamesResponse>> =
        flow {
            emit(Resource.loading(null))
            gameDao.delete(listOf(game))
            emit(loadFromDb(order))
        }.flowOn(Dispatchers.IO)

    private fun loadFromDb(order: GameOrder): Resource<GamesResponse> =
        when (order) {
            GameOrder.AVERAGE_SCORE -> Resource.success(GamesResponse(gameDao.getAllByScore()))
            GameOrder.NAME -> Resource.success(GamesResponse(gameDao.getAllByName()))
        }
}
