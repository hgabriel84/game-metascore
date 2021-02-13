package com.hgabriel.videogames.scores.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GameService {
    @GET("game/playstation-4/{name}")
    suspend fun getPS4Game(@Path("name") name: String): Response<String>
}