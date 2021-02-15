package com.hgabriel.videogames.scores.data.repository

import com.hgabriel.videogames.scores.data.local.GameDao
import com.hgabriel.videogames.scores.data.remote.GameRemoteDataSource
import com.hgabriel.videogames.scores.data.vo.GameOrder
import com.hgabriel.videogames.scores.data.vo.GamesResponse
import com.hgabriel.videogames.scores.data.vo.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GameRepository @Inject constructor(
    private val gameRemoteDataSource: GameRemoteDataSource,
    private val gameDao: GameDao
) {

    suspend fun fetchGames(order: GameOrder): Flow<Resource<GamesResponse>> {
        return flow {
            val dbResult = fetchGamesFromDb(order)
            emit(dbResult)
            emit(Resource.loading(null))
            dbResult.data?.result?.forEach { dbGame ->
                gameRemoteDataSource.fetchGame(dbGame.gamePath).data?.let { remoteGame ->
                    remoteGame.played = dbGame.played
                    gameDao.insert(remoteGame)
                }
            }
            emit(fetchGamesFromDb(order))
        }.flowOn(Dispatchers.IO)
    }

    suspend fun addGame(gamePath: String, order: GameOrder): Flow<Resource<GamesResponse>> {
        return flow {
            emit(Resource.loading(null))
            gameRemoteDataSource.fetchGame(gamePath).data?.let { remoteGame ->
                gameDao.insert(remoteGame)
            }
            emit(fetchGamesFromDb(order))
        }.flowOn(Dispatchers.IO)
    }

    private fun fetchGamesFromDb(order: GameOrder): Resource<GamesResponse> =
        when (order) {
            GameOrder.AVERAGE_SCORE -> Resource.success(GamesResponse(gameDao.getAllByScore()))
            GameOrder.NAME -> Resource.success(GamesResponse(gameDao.getAllByName()))
        }
}