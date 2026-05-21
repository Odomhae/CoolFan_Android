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

private const val SWING_FULL_MS = 4000f  // 4초에 360° Y회전 1바퀴

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

    // ── 헤드 Y축 전체 회전 (swing) ───────────────────────────────────────
    var swingAngle by remember { mutableFloatStateOf(0f) }
    val currentSwinging by rememberUpdatedState(fanState.swinging)

    LaunchedEffect(Unit) {
        val dpm = 360f / SWING_FULL_MS   // degrees per millisecond
        var lastNanos = withFrameNanos { it }
        while (true) {
            val nanos = withFrameNanos { it }
            val dt = ((nanos - lastNanos) / 1_000_000L).coerceAtMost(50L)
            lastNanos = nanos
            val step = dpm * dt

            if (currentSwinging) {
                // 시계 방향으로 연속 360° 회전
                swingAngle = (swingAngle + step) % 360f
            } else if (swingAngle != 0f) {
                // 가장 짧은 경로로 0°(정면)에 복귀 (2배속)
                val dist = if (swingAngle <= 180f) -swingAngle else 360f - swingAngle
                val move = step * 2f
                if (abs(dist) <= move) {
                    swingAngle = 0f
                } else {
                    swingAngle = ((swingAngle + if (dist < 0) -move else move) + 360f) % 360f
                }
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

        // 팬 헤드: Y축 360° 회전
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
