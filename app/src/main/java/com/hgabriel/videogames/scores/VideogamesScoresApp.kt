package com.hgabriel.videogames.scores

import android.app.Application
import timber.log.Timber

class VideogamesScoresApp : Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}
