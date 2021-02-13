package com.hgabriel.videogames.scores.data.remote

import com.hgabriel.videogames.scores.data.vo.Game
import com.hgabriel.videogames.scores.data.vo.Resource
import retrofit2.Response
import retrofit2.Retrofit
import javax.inject.Inject

class GameRemoteDataSource @Inject constructor(private val retrofit: Retrofit) {

    suspend fun fetchGame(name: String): Resource<Game> {
        val service = retrofit.create(GameService::class.java)
        return getResponse(
            request = { service.getPS4Game(name) },
            errorMessage = "Error fetching Game"
        )
    }

    private suspend fun <T> getResponse(
        request: suspend () -> Response<T>,
        errorMessage: String
    ): Resource<T> {
        val result = request.invoke()
        return if (result.isSuccessful) {
            Resource.success(result.body())
        } else {
            Resource.error(errorMessage, null)
        }
    }
}