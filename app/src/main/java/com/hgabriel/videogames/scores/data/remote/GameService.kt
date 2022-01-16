package com.hgabriel.videogames.scores.data.remote

import com.hgabriel.videogames.scores.BuildConfig
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

interface GameService {

    @POST("games")
    suspend fun searchGame(@Body body: RequestBody): Response<List<GameResponse>>

    @POST("games")
    suspend fun getGame(@Body body: RequestBody): Response<List<GameResponse>>

    @POST("covers")
    suspend fun getCoverUrl(@Body body: RequestBody): Response<List<CoverResponse>>

    companion object {
        private const val BASE_URL = "https://api.igdb.com/v4/"

        fun create(): GameService {
            val logger = HttpLoggingInterceptor().apply { level = Level.BASIC }

            val authorization = Interceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${SessionManager.token}")
                    .addHeader("Client-ID", BuildConfig.TwitchClientId)
                chain.proceed(requestBuilder.build())
            }

            val client = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS)
                .authenticator(TwitchAuthenticator())
                .addInterceptor(logger)
                .addInterceptor(authorization)
                .build()

            val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()

            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build()
                .create(GameService::class.java)
        }
    }
}
