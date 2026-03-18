package com.secta9ine.didapp.system

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import com.secta9ine.didapp.v2.contract.PowerScheduleDto
import java.util.Calendar

class PowerScheduleManager(
    private val context: Context
) {
    companion object {
        const val TAG = "PowerScheduleMgr"
        const val ACTION_POWER_OFF = "com.secta9ine.didapp.ACTION_POWER_OFF"
        const val ACTION_POWER_ON = "com.secta9ine.didapp.ACTION_POWER_ON"
        private const val REQUEST_CODE_OFF = 9010
        private const val REQUEST_CODE_ON = 9011
        private const val PREFS_NAME = "power_schedule_prefs"
        private const val KEY_ENABLED = "enabled"
        private const val KEY_OFF_TIME = "powerOffTime"
        private const val KEY_ON_TIME = "powerOnTime"
    }

    private val alarmManager get() = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

    fun applySchedule(schedule: PowerScheduleDto) {
        saveToPrefs(schedule)

        cancelAlarms()

        if (!schedule.enabled || schedule.powerOffTime == null || schedule.powerOnTime == null) {
            Log.i(TAG, "Power schedule disabled or incomplete, alarms cancelled")
            return
        }

        scheduleAlarm(schedule.powerOffTime, ACTION_POWER_OFF, REQUEST_CODE_OFF)
        scheduleAlarm(schedule.powerOnTime, ACTION_POWER_ON, REQUEST_CODE_ON)
        Log.i(TAG, "Power schedule applied: OFF=${schedule.powerOffTime}, ON=${schedule.powerOnTime}")
    }

    fun getSchedule(): PowerScheduleDto {
        return loadFromPrefs()
    }

    fun isInSleepWindow(): Boolean {
        val schedule = loadFromPrefs()
        if (!schedule.enabled || schedule.powerOffTime == null || schedule.powerOnTime == null) return false

        val offParts = schedule.powerOffTime.split(":")
        val onParts = schedule.powerOnTime.split(":")
        if (offParts.size != 2 || onParts.size != 2) return false

        val offMinutes = offParts[0].toIntOrNull()?.times(60)?.plus(offParts[1].toIntOrNull() ?: 0) ?: return false
        val onMinutes = onParts[0].toIntOrNull()?.times(60)?.plus(onParts[1].toIntOrNull() ?: 0) ?: return false

        val now = Calendar.getInstance()
        val nowMinutes = now.get(Calendar.HOUR_OF_DAY) * 60 + now.get(Calendar.MINUTE)

        return if (offMinutes < onMinutes) {
            // Same-day window, e.g., off=02:00, on=06:00
            nowMinutes in offMinutes until onMinutes
        } else {
            // Overnight window, e.g., off=23:00, on=07:00
            nowMinutes >= offMinutes || nowMinutes < onMinutes
        }
    }

    private fun scheduleAlarm(time: String, action: String, requestCode: Int) {
        val parts = time.split(":")
        if (parts.size != 2) return
        val hour = parts[0].toIntOrNull() ?: return
        val minute = parts[1].toIntOrNull() ?: return

        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= System.currentTimeMillis()) {
                add(Calendar.DAY_OF_YEAR, 1)
            }
        }

        val intent = Intent(context, PowerScheduleReceiver::class.java).apply {
            this.action = action
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alarmManager.setAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        } else {
            alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, pendingIntent)
        }

        Log.i(TAG, "Alarm scheduled: action=$action time=$time triggerAt=${calendar.time}")
    }

    private fun cancelAlarms() {
        listOf(
            REQUEST_CODE_OFF to ACTION_POWER_OFF,
            REQUEST_CODE_ON to ACTION_POWER_ON
        ).forEach { (code, action) ->
            val intent = Intent(context, PowerScheduleReceiver::class.java).apply {
                this.action = action
            }
            val pendingIntent = PendingIntent.getBroadcast(
                context, code, intent,
                PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE
            )
            pendingIntent?.let {
                alarmManager.cancel(it)
                it.cancel()
            }
        }
    }

    private fun saveToPrefs(schedule: PowerScheduleDto) {
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()
            .putBoolean(KEY_ENABLED, schedule.enabled)
            .putString(KEY_OFF_TIME, schedule.powerOffTime)
            .putString(KEY_ON_TIME, schedule.powerOnTime)
            .apply()
    }

    private fun loadFromPrefs(): PowerScheduleDto {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return PowerScheduleDto(
            didId = "",
            enabled = prefs.getBoolean(KEY_ENABLED, false),
            powerOffTime = prefs.getString(KEY_OFF_TIME, null),
            powerOnTime = prefs.getString(KEY_ON_TIME, null)
        )
    }
}
