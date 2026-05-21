package com.odom.coolfan.util

import com.odom.coolfan.model.FanSpeed

fun FanSpeed.rotationDurationMs(): Int = when (this) {
    FanSpeed.OFF -> 0
    FanSpeed.LOW -> 1200
    FanSpeed.MEDIUM -> 600
    FanSpeed.HIGH -> 300
}
