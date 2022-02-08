package com.hgabriel.gamemetascore.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Query("SELECT * FROM game order by totalRating DESC")
    fun gamesByRating(): Flow<List<Game>>

    @Query("SELECT * FROM game order by UPPER(name) ASC")
    fun gamesByName(): Flow<List<Game>>

    @Query("SELECT * FROM game where id=:id")
    suspend fun getGameById(id: Int): Game

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: Game)

    @Query("DELETE FROM game WHERE id=:id")
    suspend fun deleteById(id: Int)
}
