package com.secta9ine.didapp.v2.contract

data class PowerScheduleDto(
    val didId: String,
    val enabled: Boolean = false,
    val powerOffTime: String? = null,   // "HH:mm" format
    val powerOnTime: String? = null     // "HH:mm" format
)
