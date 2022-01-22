package com.hgabriel.gamemetascore.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM game order by totalRating DESC")
    suspend fun getGamesByRating(): List<Game>

    @Query("SELECT * FROM game order by UPPER(name) ASC")
    suspend fun getGamesByName(): List<Game>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: Game)

    @Delete
    suspend fun delete(game: Game)
}
