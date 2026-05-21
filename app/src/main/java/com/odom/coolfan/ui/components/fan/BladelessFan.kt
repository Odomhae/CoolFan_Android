package com.odom.coolfan.ui.components.fan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import com.odom.coolfan.model.ThemeColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun BladelessFanHead(
    modifier: Modifier = Modifier,
    rotationAngle: Float,
    themeColors: ThemeColors
) {
    // Ring Y-tilt: makes the ring appear to tilt as airflow direction changes
    val ringYTilt = sin(rotationAngle * PI / 180.0).toFloat() * 25f

    Box(modifier = modifier) {
        // Outer ring structure — static
        Canvas(Modifier.fillMaxSize()) {
            val cx = size.width / 2f; val cy = size.height * 0.45f
            val outerR = minOf(size.width, size.height) * 0.40f
            val innerR = outerR * 0.60f
            drawRingShell(cx, cy, outerR, innerR, themeColors)
        }
        // Flow markers — Y-tilt to simulate airflow direction
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationY = ringYTilt
                    cameraDistance = 8f * density
                }
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val cx = size.width / 2f; val cy = size.height * 0.45f
                val outerR = minOf(size.width, size.height) * 0.40f
                val innerR = outerR * 0.60f
                drawFlowMarkers(cx, cy, outerR, innerR, rotationAngle, themeColors.accent)
            }
        }
    }
}

@Composable
fun BladelessFanBody(
    modifier: Modifier = Modifier,
    themeColors: ThemeColors
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f; val cy = size.height * 0.45f
        val outerR = minOf(size.width, size.height) * 0.40f
        drawBladelessPillar(cx, cy + outerR, outerR * 0.55f, themeColors.frame)
        drawBladelessBase(cx, cy + outerR + outerR * 0.55f + 10f, outerR * 0.18f, outerR * 0.55f, themeColors.frame)
    }
}

private fun DrawScope.drawRingShell(cx: Float, cy: Float, outerR: Float, innerR: Float, themeColors: ThemeColors) {
    val thickness = outerR - innerR
    val midR = (outerR + innerR) / 2f
    drawCircle(
        brush = Brush.sweepGradient(
            colors = listOf(themeColors.frame, themeColors.accent.copy(alpha = 0.7f), themeColors.frame, themeColors.accent.copy(alpha = 0.4f), themeColors.frame),
            center = Offset(cx, cy)
        ),
        radius = midR, center = Offset(cx, cy), style = Stroke(width = thickness)
    )
    drawCircle(color = themeColors.accent, radius = outerR, center = Offset(cx, cy), style = Stroke(width = 3f))
    drawCircle(color = themeColors.accent.copy(alpha = 0.4f), radius = innerR, center = Offset(cx, cy), style = Stroke(width = 2f))
}

private fun DrawScope.drawFlowMarkers(
    cx: Float, cy: Float, outerR: Float, innerR: Float,
    rotation: Float, accentColor: Color
) {
    val midR = (outerR + innerR) / 2f
    val thickness = outerR - innerR
    rotate(rotation, pivot = Offset(cx, cy)) {
        for (i in 0 until 8) {
            val a = (i * 45.0) * PI / 180.0
            drawLine(
                color = accentColor.copy(alpha = 0.5f),
                start = Offset(cx + midR * 0.85f * cos(a).toFloat(), cy + midR * 0.85f * sin(a).toFloat()),
                end = Offset(cx + midR * 1.15f * cos(a + 0.3).toFloat(), cy + midR * 1.15f * sin(a + 0.3).toFloat()),
                strokeWidth = thickness * 0.15f
            )
        }
    }
}

private fun DrawScope.drawBladelessPillar(cx: Float, startY: Float, height: Float, color: Color) {
    drawRect(color = color, topLeft = Offset(cx - 9f, startY), size = Size(18f, height))
}

private fun DrawScope.drawBladelessBase(cx: Float, baseY: Float, height: Float, width: Float, color: Color) {
    drawOval(color = color, topLeft = Offset(cx - width, baseY), size = Size(width * 2f, height))
}
