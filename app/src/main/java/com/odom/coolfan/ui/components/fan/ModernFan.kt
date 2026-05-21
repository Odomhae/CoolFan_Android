package com.odom.coolfan.ui.components.fan

import androidx.compose.foundation.Canvas
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
import com.odom.coolfan.model.ThemeColors

@Composable
fun ModernFanHead(
    modifier: Modifier = Modifier,
    rotationAngle: Float,
    themeColors: ThemeColors
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = minOf(size.width, size.height) * 0.42f
        drawModernGrill(cx, cy, radius, themeColors.frame)
        drawModernBlades(cx, cy, radius * 0.70f, rotationAngle, themeColors.blade, themeColors.accent)
        drawModernHub(cx, cy, radius * 0.10f, themeColors.accent)
    }
}

@Composable
fun ModernFanBody(
    modifier: Modifier = Modifier,
    themeColors: ThemeColors
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = minOf(size.width, size.height) * 0.42f
        drawModernPole(cx, cy, radius, themeColors.frame)
        drawModernBase(cx, cy + radius * 1.35f, themeColors.frame)
    }
}

private fun DrawScope.drawModernGrill(cx: Float, cy: Float, radius: Float, color: Color) {
    drawCircle(color = color, radius = radius, center = Offset(cx, cy), style = Stroke(width = radius * 0.025f))
    for (i in 1..6) {
        drawCircle(
            color = color.copy(alpha = 0.2f),
            radius = radius * (i / 6f),
            center = Offset(cx, cy),
            style = Stroke(width = radius * 0.012f)
        )
    }
}

private fun DrawScope.drawModernBlades(
    cx: Float, cy: Float, bladeRadius: Float,
    rotation: Float, bladeColor: Color, accentColor: Color
) {
    for (i in 0 until 5) {
        rotate(rotation + i * 72f, pivot = Offset(cx, cy)) {
            val hw = bladeRadius * 0.10f
            val path = Path().apply {
                moveTo(cx, cy)
                lineTo(cx - hw, cy)
                lineTo(cx - hw * 0.5f, cy - bladeRadius)
                lineTo(cx + hw * 0.5f, cy - bladeRadius)
                lineTo(cx + hw, cy)
                close()
            }
            drawPath(
                path = path,
                brush = Brush.linearGradient(
                    colors = listOf(accentColor.copy(alpha = 0.8f), bladeColor.copy(alpha = 0.6f)),
                    start = Offset(cx, cy),
                    end = Offset(cx, cy - bladeRadius)
                )
            )
        }
    }
}

private fun DrawScope.drawModernHub(cx: Float, cy: Float, radius: Float, color: Color) {
    drawCircle(color = color, radius = radius, center = Offset(cx, cy))
}

private fun DrawScope.drawModernPole(cx: Float, cy: Float, radius: Float, color: Color) {
    val pw = radius * 0.05f
    drawRect(
        color = color,
        topLeft = Offset(cx - pw / 2f, cy + radius * 0.9f),
        size = Size(pw, radius * 0.5f)
    )
}

private fun DrawScope.drawModernBase(cx: Float, baseY: Float, color: Color) {
    drawRoundRect(
        color = color,
        topLeft = Offset(cx - 70f, baseY - 8f),
        size = Size(140f, 16f),
        cornerRadius = CornerRadius(8f)
    )
}
