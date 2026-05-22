package com.odom.coolfan.ui.components.fan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.graphicsLayer
import com.odom.coolfan.model.ThemeColors
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VintageFanHead(
    modifier: Modifier = Modifier,
    rotationAngle: Float,
    themeColors: ThemeColors
) {
    // Blade Y-tilt: oscillates once per full rotation → propeller depth feel
    val bladeYTilt = sin(rotationAngle * PI / 180.0).toFloat() * 35f

    Box(modifier = modifier) {
        // Grill — static within head
        Canvas(Modifier.fillMaxSize()) {
            val (cx, cy, r) = dims()
            drawVintageGrill(cx, cy, r, themeColors.frame)
        }
        // Blades — rotationY oscillates with spin angle
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationY = bladeYTilt
                    cameraDistance = 8f * density
                }
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val (cx, cy, r) = dims()
                drawVintageBlades(cx, cy, r * 0.72f, rotationAngle, themeColors.blade, themeColors.accent)
            }
        }
        // Hub — always on top
        Canvas(Modifier.fillMaxSize()) {
            val (cx, cy, r) = dims()
            drawVintageHub(cx, cy, r * 0.12f, themeColors.accent)
        }
    }
}

@Composable
fun VintageFanBody(
    modifier: Modifier = Modifier,
    themeColors: ThemeColors
) {
    Canvas(modifier = modifier) {
        val (cx, cy, r) = dims()
        drawVintagePole(cx, cy, r, themeColors.frame)
        drawVintageBase(cx, cy + r * 1.35f, r, themeColors.frame)
    }
}

private data class FanDims(val cx: Float, val cy: Float, val r: Float)
private fun DrawScope.dims() = FanDims(
    cx = size.width / 2f,
    cy = size.height / 2f,
    r = minOf(size.width, size.height) * 0.42f
)

private fun DrawScope.drawVintageGrill(cx: Float, cy: Float, radius: Float, color: Color) {
    drawCircle(color = color.copy(alpha = 0.5f), radius = radius, center = Offset(cx, cy), style = Stroke(width = radius * 0.04f))
    for (i in 1..4) {
        drawCircle(color = color.copy(alpha = 0.3f), radius = radius * (i / 4f), center = Offset(cx, cy), style = Stroke(width = radius * 0.02f))
    }
    for (i in 0 until 12) {
        val a = (i * 30.0) * PI / 180.0
        drawLine(color = color.copy(alpha = 0.3f), start = Offset(cx, cy), end = Offset(cx + radius * cos(a).toFloat(), cy + radius * sin(a).toFloat()), strokeWidth = radius * 0.02f)
    }
}

private fun DrawScope.drawVintageBlades(
    cx: Float, cy: Float, bladeR: Float,
    rotation: Float, bladeColor: Color, accentColor: Color
) {
    for (i in 0 until 3) {
        rotate(rotation + i * 120f, pivot = Offset(cx, cy)) {
            val path = Path().apply {
                val sweep = 80f
                val start = -sweep / 2f
                for (j in 0..20) {
                    val t = j / 20f
                    val a = (start + t * sweep) * PI / 180.0
                    val r = bladeR * (0.3f + 0.7f * t)
                    val dx = r * cos(a).toFloat()
                    val dy = r * sin(a).toFloat() - r * 0.15f * t
                    if (j == 0) moveTo(cx + dx, cy + dy) else lineTo(cx + dx, cy + dy)
                }
                close()
            }
            drawPath(path, Brush.radialGradient(listOf(accentColor.copy(alpha = 0.9f), bladeColor), Offset(cx, cy), bladeR))
        }
    }
}

private fun DrawScope.drawVintageHub(cx: Float, cy: Float, r: Float, color: Color) {
    drawCircle(color = color, radius = r, center = Offset(cx, cy))
    drawCircle(color = Color.White.copy(alpha = 0.3f), radius = r * 0.5f, center = Offset(cx - r * 0.2f, cy - r * 0.2f))
}

private fun DrawScope.drawVintagePole(cx: Float, cy: Float, r: Float, color: Color) {
    val pw = r * 0.08f
    drawRect(color = color, topLeft = Offset(cx - pw / 2f, cy + r * 0.9f), size = Size(pw, r * 0.5f))
}

private fun DrawScope.drawVintageBase(cx: Float, baseY: Float, r: Float, color: Color) {
    val hw = r * 0.63f
    val h = r * 0.07f
    drawRoundRect(color = color, topLeft = Offset(cx - hw, baseY - h / 2f), size = Size(hw * 2f, h), cornerRadius = CornerRadius(h / 2f))
}
