package com.bonial.codechallenge

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class MainApplication: Application() {

    // AppContainer instance used by the rest of classes to obtain dependencies
    //todo lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        //todo container = AppContainerImpl(this)
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }

}

