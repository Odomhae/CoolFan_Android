package com.odom.coolfan.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.foundation.layout.Box
import com.odom.coolfan.model.FanSpeed
import com.odom.coolfan.model.FanState
import com.odom.coolfan.model.FanStyle
import com.odom.coolfan.model.ThemeColors
import com.odom.coolfan.ui.components.fan.BladelessFan
import com.odom.coolfan.ui.components.fan.CuteFan
import com.odom.coolfan.ui.components.fan.ModernFan
import com.odom.coolfan.ui.components.fan.VintageFan
import com.odom.coolfan.util.rotationDurationMs

@Composable
fun FanCanvas(
    fanState: FanState,
    themeColors: ThemeColors,
    modifier: Modifier = Modifier
) {
    val isSpinning = fanState.speed != FanSpeed.OFF
    val durationMs = fanState.speed.rotationDurationMs().coerceAtLeast(100)

    val infiniteTransition = rememberInfiniteTransition(label = "fan")

    val rawAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = durationMs, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spin"
    )

    val displayAngle by animateFloatAsState(
        targetValue = if (isSpinning) rawAngle else rawAngle,
        animationSpec = tween(durationMillis = 300),
        label = "angle_smooth"
    )

    val speedMultiplier by animateFloatAsState(
        targetValue = if (isSpinning) 1f else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "speed_mult"
    )

    val swingRaw by infiniteTransition.animateFloat(
        initialValue = -25f,
        targetValue = 25f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 4000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "swing_raw"
    )

    val swingAngle by animateFloatAsState(
        targetValue = if (fanState.swinging) swingRaw else 0f,
        animationSpec = tween(durationMillis = 400),
        label = "swing_smooth"
    )

    Box(
        modifier = modifier.graphicsLayer { rotationZ = swingAngle },
        contentAlignment = Alignment.Center
    ) {
        val angle = displayAngle * speedMultiplier
        when (fanState.fanStyle) {
            FanStyle.VINTAGE -> VintageFan(
                modifier = Modifier.fillMaxSize(),
                rotationAngle = angle,
                themeColors = themeColors
            )
            FanStyle.MODERN -> ModernFan(
                modifier = Modifier.fillMaxSize(),
                rotationAngle = angle,
                themeColors = themeColors
            )
            FanStyle.BLADELESS -> BladelessFan(
                modifier = Modifier.fillMaxSize(),
                rotationAngle = angle,
                themeColors = themeColors
            )
            FanStyle.CUTE -> CuteFan(
                modifier = Modifier.fillMaxSize(),
                rotationAngle = angle,
                themeColors = themeColors
            )
        }
    }
}
