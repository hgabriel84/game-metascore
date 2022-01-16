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
            dbResult.data?.result?.forEach { localGame ->
                gameRemoteDataSource.getGame(localGame.id).data?.let { remoteGame ->
                    remoteGame.coverId?.let {
                        remoteGame.cover = gameRemoteDataSource.getCoverUrl(it).data
                    }
                    remoteGame.apply {
                        played = localGame.played
                        liked = localGame.liked
                    }
                    gameDao.insert(remoteGame)
                }
            }
            emit(loadFromDb(order))
        }.flowOn(Dispatchers.IO)

    suspend fun importGames(
        gamesToImport: MetaGames,
        order: GameOrder
    ): Flow<Resource<GamesResponse>> =
        flow {
            emit(Resource.loading(null))
            gamesToImport.games.forEach { gameToImport ->
                gameRemoteDataSource.getGame(gameToImport.id).data?.let { remoteGame ->
                    remoteGame.coverId?.let {
                        remoteGame.cover = gameRemoteDataSource.getCoverUrl(it).data
                    }
                    remoteGame.apply {
                        played = gameToImport.played
                        liked = gameToImport.liked
                    }
                    gameDao.insert(remoteGame)
                }
            }
            emit(loadFromDb(order))
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
            GameOrder.TOTAL_RATING -> Resource.success(GamesResponse(gameDao.getAllByScore()))
            GameOrder.NAME -> Resource.success(GamesResponse(gameDao.getAllByName()))
        }
}
