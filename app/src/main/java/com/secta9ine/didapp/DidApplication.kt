package com.secta9ine.didapp

import android.app.Application
import com.secta9ine.didapp.system.AppRelaunchScheduler
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class DidApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        installCrashRelaunchHandler()
    }

    private fun installCrashRelaunchHandler() {
        val defaultHandler = Thread.getDefaultUncaughtExceptionHandler()
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            AppRelaunchScheduler.launchImmediatelyFromForeground(this)
            AppRelaunchScheduler.schedule(this, delayMs = 1500L)
            if (defaultHandler != null) {
                defaultHandler.uncaughtException(thread, throwable)
            } else {
                android.os.Process.killProcess(android.os.Process.myPid())
            }
        }
    }
}
