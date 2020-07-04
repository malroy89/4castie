package de.lamaka.fourcastie

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentFactory
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import javax.inject.Inject

@HiltAndroidApp
class FourcastieApplication : Application() {

//    @Inject
//    lateinit var fragmentFactory: FragmentFactory

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

//        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
//            override fun onActivityPaused(p0: Activity) {
//            }
//
//            override fun onActivityStarted(p0: Activity) {
//            }
//
//            override fun onActivityDestroyed(p0: Activity) {
//            }
//
//            override fun onActivitySaveInstanceState(p0: Activity, p1: Bundle) {
//            }
//
//            override fun onActivityStopped(p0: Activity) {
//            }
//
//            override fun onActivityCreated(p0: Activity, p1: Bundle?) {
//                if (p0 is FragmentActivity) {
//                    p0.supportFragmentManager.fragmentFactory = fragmentFactory
//                }
//            }
//
//            override fun onActivityResumed(p0: Activity) {
//            }
//
//        })
    }
}