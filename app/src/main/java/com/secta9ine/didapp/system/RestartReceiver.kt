package com.secta9ine.didapp.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class RestartReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        AppRelaunchScheduler.launchNow(context)
    }
}
