package com.hgabriel.gamemetascore.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface GamesDao {

    @Query("SELECT * FROM game order by totalRating DESC")
    fun gamesByRating(): Flow<List<Game>>

    @Query("SELECT * FROM game order by UPPER(name) ASC")
    fun gamesByName(): Flow<List<Game>>

    @Query("SELECT * FROM game WHERE UPPER(name) LIKE :keyword order by totalRating DESC")
    fun gamesByRating(keyword: String): Flow<List<Game>>

    @Query("SELECT * FROM game WHERE UPPER(name) LIKE :keyword order by UPPER(name) ASC")
    fun gamesByName(keyword: String): Flow<List<Game>>

    @Query("SELECT * FROM game where id=:id")
    suspend fun getGameById(id: Int): Game

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(game: Game)

    @Query("DELETE FROM game WHERE id=:id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM game order by UPPER(name) ASC")
    suspend fun getGames(): List<Game>
}
