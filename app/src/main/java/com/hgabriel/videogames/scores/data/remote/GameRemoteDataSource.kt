package com.hgabriel.videogames.scores.data.remote

import com.hgabriel.videogames.scores.data.vo.Game
import com.hgabriel.videogames.scores.data.vo.Resource
import javax.inject.Inject

class GameRemoteDataSource @Inject constructor(private val service: GameService) {

    suspend fun searchGame(keyword: String): Resource<List<Game>> {
        val body = "fields id,name,cover,aggregated_rating,rating,total_rating; search $keyword;"
        val result = service.searchGame(body)
        return if (result.isSuccessful) {
            Resource.success(result.body()?.toGame())
        } else {
            Resource.error("Error searching game", null)
        }
    }

    suspend fun getGames(ids: List<Int>): Resource<List<Game>> {
        val body = "fields *; where id = (${ids.joinToString(separator = ",")});"
        val result = service.getGames(body)
        return if (result.isSuccessful) {
            Resource.success(result.body()?.toGame())
        } else {
            Resource.error("Error fetching games", null)
        }
    }

    suspend fun getCoverUrl(id: Int): Resource<String> {
        val body = "fields url; where game = $id;"
        val result = service.getCoverUrl(body)
        return if (result.isSuccessful) {
            Resource.success(result.body())
        } else {
            Resource.error("Error fetching game cover", null)
        }
    }

    private fun List<GameResponse>.toGame(): List<Game> =
        map {
            Game(
                id = it.id,
                name = it.name,
                coverId = it.cover,
                criticsRating = it.criticsRating,
                usersRating = it.usersRating,
                totalRating = it.totalRating
            )
        }
}
