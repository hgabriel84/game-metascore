package com.hgabriel.gamemetascore.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface GameDao {

    @Query("SELECT * FROM game order by totalRating DESC")
    suspend fun getGamesByRating(): List<Game>

    @Query("SELECT * FROM game order by UPPER(name) ASC")
    suspend fun getGamesByName(): List<Game>

    @Query("SELECT * FROM game where id=:id")
    suspend fun getGameById(id: Int): Game

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: Game)

    @Delete
    suspend fun delete(game: Game)
}
