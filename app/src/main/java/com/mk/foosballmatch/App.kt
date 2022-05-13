package com.mk.foosballmatch

import android.app.Application
import android.widget.Toast
import dagger.hilt.android.HiltAndroidApp
import io.reactivex.rxjava3.plugins.RxJavaPlugins

@HiltAndroidApp
class App: Application() {

    override fun onCreate() {
        super.onCreate()
        RxJavaPlugins.setErrorHandler {
            Toast.makeText(applicationContext, "Error: ${it.cause}", Toast.LENGTH_SHORT).show()
        }
    }
}