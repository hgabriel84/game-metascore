package com.hgabriel.videogames.scores.data.remote

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface GameService {
    @GET("{gamePath}")
    suspend fun getGame(@Path("gamePath", encoded = true) gamePath: String): Response<String>
}