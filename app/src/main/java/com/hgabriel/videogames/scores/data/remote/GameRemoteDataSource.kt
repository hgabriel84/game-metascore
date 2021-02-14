package com.hgabriel.videogames.scores.data.remote

import com.hgabriel.videogames.scores.data.vo.Game
import com.hgabriel.videogames.scores.data.vo.Resource
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class GameRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {

    suspend fun fetchGame(gamePath: String): Resource<Game> {
        val service = retrofit.create(GameService::class.java)
        return getGameFromPlainText(
            gamePath = gamePath,
            request = { service.getGame(gamePath) },
            errorMessage = "Error fetching Game"
        )
    }

    private suspend fun getGameFromPlainText(
        gamePath: String,
        request: suspend () -> Response<String>,
        errorMessage: String
    ): Resource<Game> {
        val result = request.invoke()
        return if (result.isSuccessful) {
            Resource.success(result.body()?.toGame(gamePath))
        } else {
            Resource.error(errorMessage, null)
        }
    }
}