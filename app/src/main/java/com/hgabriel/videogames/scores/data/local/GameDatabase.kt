package com.hgabriel.videogames.scores.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hgabriel.videogames.scores.data.vo.Game

@Database(entities = [Game::class], version = 1)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}