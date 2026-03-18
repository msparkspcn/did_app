package com.secta9ine.didapp.system

import android.app.AlarmManager
import android.app.ActivityOptions
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.SystemClock
import android.util.Log

object AppRelaunchScheduler {
    private const val RESTART_REQUEST_CODE = 9001
    private const val BOOT_LAUNCH_REQUEST_CODE = 9002
    private const val TAG = "AppRelaunchScheduler"

    fun schedule(context: Context, delayMs: Long = 1500L) {
        val appContext = context.applicationContext
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerAt = SystemClock.elapsedRealtime() + delayMs
        val restartIntent = Intent(appContext, com.secta9ine.didapp.ui.MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        val pendingIntent = PendingIntent.getActivity(
            appContext,
            RESTART_REQUEST_CODE,
            restartIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            pendingIntentOptions()
        )
        Log.i(TAG, "schedule crash relaunch in ${delayMs}ms")

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAt, pendingIntent)
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAt, pendingIntent)
        }
    }

    fun launchImmediatelyFromForeground(context: Context): Boolean {
        return runCatching {
            val appContext = context.applicationContext
            val restartIntent = Intent(appContext, com.secta9ine.didapp.ui.MainActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            val pendingIntent = PendingIntent.getActivity(
                appContext,
                RESTART_REQUEST_CODE,
                restartIntent,
                PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,
                pendingIntentOptions()
            )
            pendingIntent.send()
            Log.i(TAG, "immediate relaunch request sent")
            true
        }.getOrElse { error ->
            Log.e(TAG, "immediate relaunch failed: ${error.message}", error)
            false
        }
    }

    fun launchNow(context: Context) {
        try {
            context.startActivity(
                Intent(context, com.secta9ine.didapp.ui.MainActivity::class.java).apply {
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                }
            )
        } catch (t: Throwable) {
            Log.e(TAG, "launchNow failed: ${t.message}", t)
            scheduleBootLaunch(context, delayMs = 2500L)
        }
    }

    fun scheduleBootLaunch(context: Context, delayMs: Long = 4000L) {
        val appContext = context.applicationContext
        val alarmManager = appContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val triggerAt = SystemClock.elapsedRealtime() + delayMs
        val launchIntent = Intent(appContext, com.secta9ine.didapp.ui.MainActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
        }
        val launchPendingIntent = PendingIntent.getActivity(
            appContext,
            BOOT_LAUNCH_REQUEST_CODE,
            launchIntent,
            PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            pendingIntentOptions()
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.i(TAG, "schedule boot launch in ${delayMs}ms")
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME_WAKEUP,
                triggerAt,
                launchPendingIntent
            )
        } else {
            alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAt, launchPendingIntent)
        }
    }

    private fun pendingIntentOptions(): Bundle? {
        return when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE -> {
                val options = ActivityOptions.makeBasic()
                options.setPendingIntentBackgroundActivityStartMode(
                    ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
                )
                options.setPendingIntentCreatorBackgroundActivityStartMode(
                    ActivityOptions.MODE_BACKGROUND_ACTIVITY_START_ALLOWED
                )
                options.toBundle()
            }
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> {
                val options = ActivityOptions.makeBasic()
                options.setPendingIntentBackgroundActivityLaunchAllowed(true)
                options.toBundle()
            }
            else -> null
        }
    }
}
