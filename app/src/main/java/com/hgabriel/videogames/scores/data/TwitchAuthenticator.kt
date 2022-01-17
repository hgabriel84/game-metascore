package com.hgabriel.videogames.scores.data

import com.hgabriel.videogames.scores.BuildConfig
import com.hgabriel.videogames.scores.api.TwitchService
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TwitchAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val token = runBlocking { refreshToken() }
        return response.request.newBuilder().header("Authorization", "Bearer $token")
            .build()
    }

    private suspend fun refreshToken(): String {
        val token = TwitchService.create()
            .getToken(BuildConfig.TWITCH_CLIENT_ID, BuildConfig.TWITCH_CLIENT_SECRET)
            .body()?.accessToken ?: ""
        SessionManager.token = token

        return token
    }
}
