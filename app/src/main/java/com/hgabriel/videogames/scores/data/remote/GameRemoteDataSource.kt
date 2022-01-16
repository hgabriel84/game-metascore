package com.hgabriel.videogames.scores.data.remote

import com.hgabriel.videogames.scores.data.vo.Game
import com.hgabriel.videogames.scores.data.vo.Resource
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

class GameRemoteDataSource @Inject constructor(private val service: GameService) {

    suspend fun searchGame(keyword: String): Resource<List<Game>> {
        val body =
            "fields id,name,cover,aggregated_rating,rating,total_rating; search \"$keyword\";"
        val result = service.searchGame(body.toRequestBody())
        return if (result.isSuccessful) {
            Resource.success(result.body()?.toGames())
        } else {
            Resource.error("Error searching game", null)
        }
    }

    suspend fun getGame(id: Int): Resource<Game> {
        val body = "fields id,name,cover,aggregated_rating,rating,total_rating; where id = ($id);"
        val result = service.getGame(body.toRequestBody())
        return if (result.isSuccessful) {
            result.body()?.toGame()?.let { Resource.success(it) }
                ?: Resource.error("Error fetching game", null)
        } else {
            Resource.error("Error fetching game", null)
        }
    }

    suspend fun getCoverUrl(id: Int): Resource<String> {
        val body = "fields image_id; where id = $id;"
        val result = service.getCoverUrl(body.toRequestBody())
        return if (result.isSuccessful) {
            result.body()?.toCoverUrl()?.let { Resource.success(it) }
                ?: Resource.error("Error fetching game cover", null)
        } else {
            Resource.error("Error fetching game cover", null)
        }
    }

    private fun List<GameResponse>.toGames(): List<Game> = map { it.toGame() }

    private fun List<GameResponse>.toGame(): Game? = firstOrNull()?.toGame()

    private fun GameResponse.toGame() =
        Game(
            id = id,
            name = name,
            coverId = cover,
            criticsRating = criticsRating,
            usersRating = usersRating,
            totalRating = totalRating
        )

    private fun List<CoverResponse>.toCoverUrl(): String? =
        firstOrNull()?.imageId?.let { "https://images.igdb.com/igdb/image/upload/t_cover_big/$it.png" }
}
