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
import kotlin.math.sin

@Composable
fun ModernFanHead(
    modifier: Modifier = Modifier,
    rotationAngle: Float,
    themeColors: ThemeColors
) {
    val bladeYTilt = sin(rotationAngle * PI / 180.0).toFloat() * 35f

    Box(modifier = modifier) {
        Canvas(Modifier.fillMaxSize()) {
            val cx = size.width / 2f; val cy = size.height / 2f
            val r = minOf(size.width, size.height) * 0.42f
            drawModernGrill(cx, cy, r, themeColors.frame)
        }
        Box(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    rotationY = bladeYTilt
                    cameraDistance = 8f * density
                }
        ) {
            Canvas(Modifier.fillMaxSize()) {
                val cx = size.width / 2f; val cy = size.height / 2f
                val r = minOf(size.width, size.height) * 0.42f
                drawModernBlades(cx, cy, r * 0.70f, rotationAngle, themeColors.blade, themeColors.accent)
            }
        }
        Canvas(Modifier.fillMaxSize()) {
            val cx = size.width / 2f; val cy = size.height / 2f
            val r = minOf(size.width, size.height) * 0.42f
            drawModernHub(cx, cy, r * 0.10f, themeColors.accent)
        }
    }
}

@Composable
fun ModernFanBody(
    modifier: Modifier = Modifier,
    themeColors: ThemeColors
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f; val cy = size.height / 2f
        val r = minOf(size.width, size.height) * 0.42f
        drawModernPole(cx, cy, r, themeColors.frame)
        drawModernBase(cx, cy + r * 1.35f, themeColors.frame)
    }
}

private fun DrawScope.drawModernGrill(cx: Float, cy: Float, r: Float, color: Color) {
    drawCircle(color = color, radius = r, center = Offset(cx, cy), style = Stroke(width = r * 0.025f))
    for (i in 1..6) {
        drawCircle(color = color.copy(alpha = 0.2f), radius = r * (i / 6f), center = Offset(cx, cy), style = Stroke(width = r * 0.012f))
    }
}

private fun DrawScope.drawModernBlades(
    cx: Float, cy: Float, bladeR: Float,
    rotation: Float, bladeColor: Color, accentColor: Color
) {
    for (i in 0 until 5) {
        rotate(rotation + i * 72f, pivot = Offset(cx, cy)) {
            val hw = bladeR * 0.10f
            val path = Path().apply {
                moveTo(cx, cy); lineTo(cx - hw, cy)
                lineTo(cx - hw * 0.5f, cy - bladeR); lineTo(cx + hw * 0.5f, cy - bladeR)
                lineTo(cx + hw, cy); close()
            }
            drawPath(path, Brush.linearGradient(listOf(accentColor.copy(alpha = 0.8f), bladeColor.copy(alpha = 0.6f)), Offset(cx, cy), Offset(cx, cy - bladeR)))
        }
    }
}

private fun DrawScope.drawModernHub(cx: Float, cy: Float, r: Float, color: Color) {
    drawCircle(color = color, radius = r, center = Offset(cx, cy))
}

private fun DrawScope.drawModernPole(cx: Float, cy: Float, r: Float, color: Color) {
    val pw = r * 0.05f
    drawRect(color = color, topLeft = Offset(cx - pw / 2f, cy + r * 0.9f), size = Size(pw, r * 0.5f))
}

private fun DrawScope.drawModernBase(cx: Float, baseY: Float, color: Color) {
    drawRoundRect(color = color, topLeft = Offset(cx - 70f, baseY - 8f), size = Size(140f, 16f), cornerRadius = CornerRadius(8f))
}
