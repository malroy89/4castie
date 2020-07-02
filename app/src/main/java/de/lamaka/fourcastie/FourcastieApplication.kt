package de.lamaka.fourcastie

import android.app.Application
import timber.log.Timber

class FourcastieApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())
    }
}