package com.hgabriel.gamemetascore.data

import com.hgabriel.gamemetascore.api.IgdbService
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IgdbDataSource @Inject constructor(private val igdbService: IgdbService) {

    suspend fun getGameDetail(id: Int): Resource<IgdbGame> {
        val body =
            "fields id,name,storyline,summary,cover,aggregated_rating,rating,total_rating; where id = ($id);"
        val result = igdbService.getGame(body.toRequestBody())
        return if (result.isSuccessful) {
            result.body()?.toIgdbGame()?.let { Resource.success(it) }
                ?: Resource.error("Error fetching game")
        } else {
            Resource.error("Error fetching game")
        }
    }

    suspend fun searchGame(keyword: String): Resource<List<IgdbGame>> {
        val body =
            "fields id,name,storyline,summary,cover,aggregated_rating,rating,total_rating; search \"$keyword\";"
        val result = igdbService.searchGame(body.toRequestBody())
        return if (result.isSuccessful) {
            result.body()?.toIgdbGames()?.let { Resource.success(it) }
                ?: Resource.error("Error searching game")
        } else {
            Resource.error("Error searching game")
        }
    }

    private suspend fun getCoverUrl(id: Int): String? {
        val body = "fields image_id; where id = $id;"
        val result = igdbService.getCoverUrl(body.toRequestBody())
        return if (result.isSuccessful) result.body()?.toCoverUrl() else null
    }

    private suspend fun List<IgdbGameResponse>.toIgdbGames(): List<IgdbGame> =
        map { it.toIgdbGame() }

    private suspend fun List<IgdbGameResponse>.toIgdbGame(): IgdbGame? = firstOrNull()?.toIgdbGame()

    private suspend fun IgdbGameResponse.toIgdbGame() =
        IgdbGame(
            id = id,
            name = name,
            storyline = storyline,
            summary = summary,
            coverId = cover,
            cover = cover?.let { getCoverUrl(cover) },
            criticsRating = criticsRating,
            usersRating = usersRating,
            totalRating = totalRating
        )

    private fun List<CoverResponse>.toCoverUrl(): String? =
        firstOrNull()?.imageId?.let { "https://images.igdb.com/igdb/image/upload/t_cover_big/$it.png" }
}
