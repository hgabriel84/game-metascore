package com.hgabriel.gamemetascore.di

import com.hgabriel.gamemetascore.api.IgdbService
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
    fun provideIgdbService() = IgdbService.create()
}
