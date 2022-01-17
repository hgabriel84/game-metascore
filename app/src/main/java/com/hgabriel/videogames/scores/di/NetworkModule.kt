package com.hgabriel.videogames.scores.di

import com.hgabriel.videogames.scores.api.IgdbService
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
    fun provideIgdbService(): IgdbService = IgdbService.create()

}
