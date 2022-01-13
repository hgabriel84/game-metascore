package com.hgabriel.videogames.scores.di

import com.hgabriel.videogames.scores.data.remote.GameService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideGameService(): GameService = GameService.create()
}
