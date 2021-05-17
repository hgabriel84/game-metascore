package com.hgabriel.videogames.scores.di

import android.content.Context
import androidx.room.Room
import com.hgabriel.videogames.scores.data.local.GameDao
import com.hgabriel.videogames.scores.data.local.GameDatabase
import com.hgabriel.videogames.scores.data.local.migrations.MIGRATION_1_2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideGameDatabase(@ApplicationContext appContext: Context): GameDatabase =
        Room
            .databaseBuilder(appContext, GameDatabase::class.java, "games.db")
            .addMigrations(MIGRATION_1_2)
            .build()

    @Provides
    fun provideGameDao(db: GameDatabase): GameDao = db.gameDao()
}