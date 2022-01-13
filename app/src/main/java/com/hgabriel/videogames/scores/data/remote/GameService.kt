package com.hgabriel.videogames.scores.data.remote

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface GameService {

    @POST("games")
    suspend fun searchGame(@Body body: String): Response<List<GameResponse>>

    @POST("games")
    suspend fun getGames(@Body body: String): Response<List<GameResponse>>

    @POST("covers")
    suspend fun getCoverUrl(@Body body: String): Response<String>

    companion object {
        private const val BASE_URL = "https://api.igdb.com/v4/"

        fun create(): GameService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val client = OkHttpClient.Builder()
                .addInterceptor(logger)
                .build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build()
                .create(GameService::class.java)
        }
    }
}
