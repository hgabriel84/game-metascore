package com.hgabriel.gamemetascore.data

import com.hgabriel.gamemetascore.api.IgdbService
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class IgdbDataSource @Inject constructor(private val igdbService: IgdbService) {

    suspend fun gamesDetail(ids: List<Int>): Resource<List<IgdbGame>> {
        val idsString = ids.joinToString(", ")
        val body =
            "fields id,name,storyline,summary,cover,aggregated_rating,rating,total_rating; where id = ($idsString); limit ${ids.size};"
        val result = igdbService.getGame(body.toRequestBody())
        return if (result.isSuccessful) {
            val covers = coverUrl(ids)
            result.body()?.toIgdbGames(covers)?.let { Resource.success(it) }
                ?: Resource.error("Error fetching games")
        } else {
            Resource.error("Error fetching games")
        }
    }

    suspend fun searchGame(keyword: String): Resource<List<IgdbGame>> {
        val body =
            "fields id,name,storyline,summary,cover,aggregated_rating,rating,total_rating; search \"$keyword\"; where platforms = (48, 167);"
        val result = igdbService.searchGame(body.toRequestBody())
        return if (result.isSuccessful) {
            val ids = result.body()?.map { it.id } ?: emptyList()
            val covers = coverUrl(ids)
            result.body()?.toIgdbGames(covers)?.let { Resource.success(it) }
                ?: Resource.error("Error searching game")
        } else {
            Resource.error("Error searching game")
        }
    }

    private suspend fun coverUrl(ids: List<Int>): Map<Int, String?> {
        val idsString = ids.joinToString(", ")
        val body = "fields image_id,game; where game = ($idsString); limit ${ids.size};"
        val result = igdbService.getCoverUrl(body.toRequestBody())
        return if (result.isSuccessful) {
            val covers = result.body() ?: emptyList()
            ids.associateWith { id ->
                covers.firstOrNull { it.game == id.toString() }?.imageId
            }
        } else {
            ids.associateWith { null }
        }
    }

    private fun List<IgdbGameResponse>.toIgdbGames(covers: Map<Int, String?>) =
        map { it.toIgdbGame(covers) }

    private fun IgdbGameResponse.toIgdbGame(covers: Map<Int, String?>) =
        IgdbGame(
            id = id,
            name = name,
            storyline = storyline,
            summary = summary,
            coverId = cover,
            cover = covers[id]?.let { "https://images.igdb.com/igdb/image/upload/t_cover_big/$it.png" },
            criticsRating = criticsRating,
            usersRating = usersRating,
            totalRating = totalRating
        )

}
