package com.odom.coolfan.ui.components.fan

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import com.odom.coolfan.model.ThemeColors
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun VintageFan(
    modifier: Modifier = Modifier,
    rotationAngle: Float,
    themeColors: ThemeColors
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = minOf(size.width, size.height) * 0.42f

        drawGrill(cx, cy, radius, themeColors.frame)
        drawVintageBlades(cx, cy, radius * 0.72f, rotationAngle, themeColors.blade, themeColors.accent)
        drawHub(cx, cy, radius * 0.12f, themeColors.accent)
        drawPole(cx, cy, radius, themeColors.frame)
        drawBase(cx, cy + radius * 1.35f, themeColors.frame)
    }
}

private fun DrawScope.drawGrill(cx: Float, cy: Float, radius: Float, color: Color) {
    drawCircle(
        color = color.copy(alpha = 0.5f),
        radius = radius,
        center = Offset(cx, cy),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = radius * 0.04f)
    )
    val rings = 4
    for (i in 1..rings) {
        drawCircle(
            color = color.copy(alpha = 0.3f),
            radius = radius * (i.toFloat() / rings),
            center = Offset(cx, cy),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = radius * 0.02f)
        )
    }
    val spokes = 12
    for (i in 0 until spokes) {
        val angle = (i * 360f / spokes) * (Math.PI / 180f)
        drawLine(
            color = color.copy(alpha = 0.3f),
            start = Offset(cx, cy),
            end = Offset(
                cx + radius * cos(angle).toFloat(),
                cy + radius * sin(angle).toFloat()
            ),
            strokeWidth = radius * 0.02f
        )
    }
}

private fun DrawScope.drawVintageBlades(
    cx: Float, cy: Float, bladeRadius: Float,
    rotation: Float, bladeColor: Color, accentColor: Color
) {
    val bladeCount = 3
    for (i in 0 until bladeCount) {
        val baseAngle = rotation + i * (360f / bladeCount)
        rotate(baseAngle, pivot = Offset(cx, cy)) {
            val path = Path().apply {
                moveTo(cx, cy)
                val sweep = 80f
                val startAngle = -sweep / 2f
                for (j in 0..20) {
                    val t = j / 20f
                    val a = (startAngle + t * sweep) * Math.PI / 180f
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

private fun DrawScope.drawHub(cx: Float, cy: Float, radius: Float, color: Color) {
    drawCircle(color = color, radius = radius, center = Offset(cx, cy))
    drawCircle(
        color = Color.White.copy(alpha = 0.3f),
        radius = radius * 0.5f,
        center = Offset(cx - radius * 0.2f, cy - radius * 0.2f)
    )
}

private fun DrawScope.drawPole(cx: Float, cy: Float, radius: Float, color: Color) {
    val poleWidth = radius * 0.08f
    drawRect(
        color = color,
        topLeft = Offset(cx - poleWidth / 2f, cy + radius * 0.9f),
        size = Size(poleWidth, radius * 0.5f)
    )
}

private fun DrawScope.drawBase(cx: Float, baseY: Float, color: Color) {
    val baseWidth = 80f
    val baseHeight = 18f
    drawRoundRect(
        color = color,
        topLeft = Offset(cx - baseWidth, baseY - baseHeight / 2f),
        size = Size(baseWidth * 2f, baseHeight),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(baseHeight / 2f)
    )
}
