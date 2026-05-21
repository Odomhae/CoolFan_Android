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
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VintageFanHead(
    modifier: Modifier = Modifier,
    rotationAngle: Float,
    themeColors: ThemeColors
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = minOf(size.width, size.height) * 0.42f
        drawVintageGrill(cx, cy, radius, themeColors.frame)
        drawVintageBlades(cx, cy, radius * 0.72f, rotationAngle, themeColors.blade, themeColors.accent)
        drawVintageHub(cx, cy, radius * 0.12f, themeColors.accent)
    }
}

@Composable
fun VintageFanBody(
    modifier: Modifier = Modifier,
    themeColors: ThemeColors
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = minOf(size.width, size.height) * 0.42f
        drawVintagePole(cx, cy, radius, themeColors.frame)
        drawVintageBase(cx, cy + radius * 1.35f, themeColors.frame)
    }
}

private fun DrawScope.drawVintageGrill(cx: Float, cy: Float, radius: Float, color: Color) {
    drawCircle(
        color = color.copy(alpha = 0.5f),
        radius = radius,
        center = Offset(cx, cy),
        style = Stroke(width = radius * 0.04f)
    )
    for (i in 1..4) {
        drawCircle(
            color = color.copy(alpha = 0.3f),
            radius = radius * (i / 4f),
            center = Offset(cx, cy),
            style = Stroke(width = radius * 0.02f)
        )
    }
    for (i in 0 until 12) {
        val angle = (i * 30f) * (Math.PI / 180f)
        drawLine(
            color = color.copy(alpha = 0.3f),
            start = Offset(cx, cy),
            end = Offset(cx + radius * cos(angle).toFloat(), cy + radius * sin(angle).toFloat()),
            strokeWidth = radius * 0.02f
        )
    }
}

private fun DrawScope.drawVintageBlades(
    cx: Float, cy: Float, bladeRadius: Float,
    rotation: Float, bladeColor: Color, accentColor: Color
) {
    for (i in 0 until 3) {
        rotate(rotation + i * 120f, pivot = Offset(cx, cy)) {
            val path = Path().apply {
                val sweep = 80f
                val startAngle = -sweep / 2f
                for (j in 0..20) {
                    val t = j / 20f
                    val a = (startAngle + t * sweep) * Math.PI / 180.0
                    val r = bladeRadius * (0.3f + 0.7f * t)
                    val dx = r * cos(a).toFloat()
                    val dy = r * sin(a).toFloat() - r * 0.15f * t
                    if (j == 0) moveTo(cx + dx, cy + dy) else lineTo(cx + dx, cy + dy)
                }
                close()
            }
            drawPath(
                path = path,
                brush = Brush.radialGradient(
                    colors = listOf(accentColor.copy(alpha = 0.9f), bladeColor),
                    center = Offset(cx, cy),
                    radius = bladeRadius
                )
            )
        }
    }
}

private fun DrawScope.drawVintageHub(cx: Float, cy: Float, radius: Float, color: Color) {
    drawCircle(color = color, radius = radius, center = Offset(cx, cy))
    drawCircle(
        color = Color.White.copy(alpha = 0.3f),
        radius = radius * 0.5f,
        center = Offset(cx - radius * 0.2f, cy - radius * 0.2f)
    )
}

private fun DrawScope.drawVintagePole(cx: Float, cy: Float, radius: Float, color: Color) {
    val pw = radius * 0.08f
    drawRect(
        color = color,
        topLeft = Offset(cx - pw / 2f, cy + radius * 0.9f),
        size = Size(pw, radius * 0.5f)
    )
}

private fun DrawScope.drawVintageBase(cx: Float, baseY: Float, color: Color) {
    drawRoundRect(
        color = color,
        topLeft = Offset(cx - 80f, baseY - 9f),
        size = Size(160f, 18f),
        cornerRadius = CornerRadius(9f)
    )
}
