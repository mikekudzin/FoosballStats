package com.mk.foosballmatch

import android.app.Application
import android.util.Log
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.rxjava3.plugins.RxJavaPlugins

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler {
            Log.d("RxError", "Error: $it")
        }
    }
}