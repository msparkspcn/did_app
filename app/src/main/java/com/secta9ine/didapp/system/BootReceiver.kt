package com.secta9ine.didapp.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class BootReceiver : BroadcastReceiver() {
    private val tag = "BootReceiver"

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_BOOT_COMPLETED,
            Intent.ACTION_LOCKED_BOOT_COMPLETED,
            Intent.ACTION_MY_PACKAGE_REPLACED -> {
                Log.i(tag, "Boot action received: ${intent.action}")
                // Re-apply power schedule alarms (AlarmManager alarms are lost on reboot)
                val powerManager = PowerScheduleManager(context.applicationContext)
                powerManager.applySchedule(powerManager.getSchedule())
                AppRelaunchScheduler.scheduleBootLaunch(context)
            }
        }
    }
}
