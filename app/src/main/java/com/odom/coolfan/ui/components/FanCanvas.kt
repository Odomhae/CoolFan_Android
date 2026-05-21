package com.odom.coolfan.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import com.odom.coolfan.model.FanSpeed
import com.odom.coolfan.model.FanState
import com.odom.coolfan.model.FanStyle
import com.odom.coolfan.model.ThemeColors
import com.odom.coolfan.ui.components.fan.BladelessFanBody
import com.odom.coolfan.ui.components.fan.BladelessFanHead
import com.odom.coolfan.ui.components.fan.CuteFanBody
import com.odom.coolfan.ui.components.fan.CuteFanHead
import com.odom.coolfan.ui.components.fan.ModernFanBody
import com.odom.coolfan.ui.components.fan.ModernFanHead
import com.odom.coolfan.ui.components.fan.VintageFanBody
import com.odom.coolfan.ui.components.fan.VintageFanHead
import com.odom.coolfan.util.rotationDurationMs

@Composable
fun FanCanvas(
    fanState: FanState,
    themeColors: ThemeColors,
    modifier: Modifier = Modifier
) {
    // Frame-by-frame rotation: responds to speed changes instantly
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    val currentSpeed by rememberUpdatedState(fanState.speed)

    LaunchedEffect(Unit) {
        var lastNanos = withFrameNanos { it }
        while (true) {
            val frameNanos = withFrameNanos { it }
            val deltaMs = ((frameNanos - lastNanos) / 1_000_000L).coerceAtMost(50L)
            lastNanos = frameNanos
            val spd = currentSpeed
            if (spd != FanSpeed.OFF) {
                val degreesPerMs = 360f / spd.rotationDurationMs()
                rotationAngle = (rotationAngle + degreesPerMs * deltaMs) % 360f
            }
        }
    }

    // Swing: head-only Y-axis rotation (-90° ↔ +90° = total 180°)
    val infiniteTransition = rememberInfiniteTransition(label = "swing")
    val swingRaw by infiniteTransition.animateFloat(
        initialValue = -90f,
        targetValue = 90f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "swing_raw"
    )
    val swingAngle by animateFloatAsState(
        targetValue = if (fanState.swinging) swingRaw else 0f,
        animationSpec = tween(durationMillis = 500),
        label = "swing_smooth"
    )

    Box(modifier = modifier) {
        // Static body: pole + base (never rotates)
        when (fanState.fanStyle) {
            FanStyle.VINTAGE  -> VintageFanBody(Modifier.fillMaxSize(), themeColors)
            FanStyle.MODERN   -> ModernFanBody(Modifier.fillMaxSize(), themeColors)
            FanStyle.BLADELESS -> BladelessFanBody(Modifier.fillMaxSize(), themeColors)
            FanStyle.CUTE     -> CuteFanBody(Modifier.fillMaxSize(), themeColors)
        }

        // Fan head: rotates on Y axis for realistic swing
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationY = swingAngle
                    cameraDistance = 12f * density
                }
        ) {
            when (fanState.fanStyle) {
                FanStyle.VINTAGE  -> VintageFanHead(Modifier.fillMaxSize(), rotationAngle, themeColors)
                FanStyle.MODERN   -> ModernFanHead(Modifier.fillMaxSize(), rotationAngle, themeColors)
                FanStyle.BLADELESS -> BladelessFanHead(Modifier.fillMaxSize(), rotationAngle, themeColors)
                FanStyle.CUTE     -> CuteFanHead(Modifier.fillMaxSize(), rotationAngle, themeColors)
            }
        }
    }
}
