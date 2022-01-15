package com.hgabriel.videogames.scores.data.remote

import com.hgabriel.videogames.scores.BuildConfig
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route

class TwitchAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request {
        val token = refreshToken()
        return response.request.newBuilder().header("Authorization", "Bearer $token")
            .build()
    }

    private fun refreshToken(): String {
        val token = TwitchService.create()
            .getToken(BuildConfig.TwitchClientId, BuildConfig.TwitchClientSecret)
            .execute()
            .body()?.accessToken ?: ""
        SessionManager.token = token

        return token
    }
}
