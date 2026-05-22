package com.odom.coolfan.ui.components

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
import kotlin.math.abs
import kotlin.math.sign
import kotlin.math.sin

private const val SWING_PERIOD_MS = 4000f
private const val SWING_AMPLITUDE = 45f
private const val RETURN_DEG_PER_MS = 0.09f

@Composable
fun FanCanvas(
    fanState: FanState,
    themeColors: ThemeColors,
    modifier: Modifier = Modifier
) {
    // ── 블레이드 Z축 회전 (속도 즉각 반응) ──────────────────────────────
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    val currentSpeed by rememberUpdatedState(fanState.speed)

    LaunchedEffect(Unit) {
        var lastNanos = withFrameNanos { it }
        while (true) {
            val nanos = withFrameNanos { it }
            val dt = ((nanos - lastNanos) / 1_000_000L).coerceAtMost(50L)
            lastNanos = nanos
            val spd = currentSpeed
            if (spd != FanSpeed.OFF) {
                rotationAngle = (rotationAngle + 360f / spd.rotationDurationMs() * dt) % 360f
            }
        }
    }

    // ── 헤드 Y축 좌우 진동 (swing) ──────────────────────────────────────
    var swingAngle by remember { mutableFloatStateOf(0f) }
    var swingPhase by remember { mutableFloatStateOf(0f) }

    LaunchedEffect(fanState.swinging) {
        var lastNanos = withFrameNanos { it }
        if (fanState.swinging) {
            swingPhase = 0f
            while (true) {
                val nanos = withFrameNanos { it }
                val dt = ((nanos - lastNanos) / 1_000_000L).coerceAtMost(50L)
                lastNanos = nanos
                swingPhase = (swingPhase + dt) % SWING_PERIOD_MS
                swingAngle = SWING_AMPLITUDE *
                    sin(2.0 * Math.PI * swingPhase / SWING_PERIOD_MS).toFloat()
            }
        } else {
            while (swingAngle != 0f) {
                val nanos = withFrameNanos { it }
                val dt = ((nanos - lastNanos) / 1_000_000L).coerceAtMost(50L)
                lastNanos = nanos
                val step = RETURN_DEG_PER_MS * dt
                if (abs(swingAngle) <= step) { swingAngle = 0f; break }
                swingAngle -= step * sign(swingAngle)
            }
        }
    }

    Box(modifier = modifier) {
        // 폴대 + 받침 (정적, 절대 회전 안 함)
        when (fanState.fanStyle) {
            FanStyle.VINTAGE   -> VintageFanBody(Modifier.fillMaxSize(), themeColors)
            FanStyle.MODERN    -> ModernFanBody(Modifier.fillMaxSize(), themeColors)
            FanStyle.BLADELESS -> BladelessFanBody(Modifier.fillMaxSize(), themeColors)
            FanStyle.CUTE      -> CuteFanBody(Modifier.fillMaxSize(), themeColors)
        }

        // 팬 헤드: Y축 좌우 진동
        Box(
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationY = swingAngle
                    cameraDistance = 12f * density
                }
        ) {
            when (fanState.fanStyle) {
                FanStyle.VINTAGE   -> VintageFanHead(Modifier.fillMaxSize(), rotationAngle, themeColors)
                FanStyle.MODERN    -> ModernFanHead(Modifier.fillMaxSize(), rotationAngle, themeColors)
                FanStyle.BLADELESS -> BladelessFanHead(Modifier.fillMaxSize(), rotationAngle, themeColors)
                FanStyle.CUTE      -> CuteFanHead(Modifier.fillMaxSize(), rotationAngle, themeColors)
            }
        }
    }
}
