package com.secta9ine.didapp.system

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.content.edit

class PowerScheduleReceiver : BroadcastReceiver() {
    companion object {
        const val TAG = "PowerScheduleRcv"
        const val PREFS_NAME = "power_schedule_state"
        const val KEY_IS_SLEEPING = "is_sleeping"
        const val KEY_STATE_CHANGED_AT = "state_changed_at"
    }

    override fun onReceive(context: Context, intent: Intent) {
        val action = intent.action ?: return
        Log.i(TAG, "Received action: $action")

        val isSleeping = when (action) {
            PowerScheduleManager.ACTION_POWER_OFF -> true
            PowerScheduleManager.ACTION_POWER_ON -> false
            else -> return
        }

        // Persist the power state so the app can read it
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit {
            putBoolean(KEY_IS_SLEEPING, isSleeping)
                .putLong(KEY_STATE_CHANGED_AT, System.currentTimeMillis())
        }

        // Re-schedule the next day's alarm
        val manager = PowerScheduleManager(context.applicationContext)
        manager.applySchedule(manager.getSchedule())

        if (!isSleeping) {
            // Reboot via QUBER AIDL (no root required)
            Log.i(TAG, "Power-on time reached, rebooting device via Quber Agent...")
            val quberManager = QuberAgentManager(
                context.applicationContext,
                com.google.gson.Gson(),
                FileLogger(context.applicationContext)
            )
            quberManager.bind()
            // Give binding a moment, then send reboot
            android.os.Handler(android.os.Looper.getMainLooper()).postDelayed({
                if (quberManager.isConnected) {
                    quberManager.reboot()
                } else {
                    Log.w(TAG, "Quber Agent not connected, falling back to su reboot")
                    try {
                        Runtime.getRuntime().exec(arrayOf("su", "-c", "reboot"))
                    } catch (e: Exception) {
                        Log.e(TAG, "su reboot also failed, relaunching app", e)
                        AppRelaunchScheduler.launchNow(context)
                    }
                }
            }, 2000)
        } else {
            Log.i(TAG, "Power state changed: sleeping=true, next alarm rescheduled")
        }
    }
}
