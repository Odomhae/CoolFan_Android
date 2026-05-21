package com.odom.coolfan.ui.components.fan

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.rotate
import com.odom.coolfan.model.ThemeColors
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun ModernFan(
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
        drawHub(cx, cy, radius * 0.10f, themeColors.accent)
        drawPole(cx, cy, radius, themeColors.frame, slim = true)
        drawBase(cx, cy + radius * 1.35f, themeColors.frame)
    }
}

private fun DrawScope.drawModernGrill(cx: Float, cy: Float, radius: Float, color: Color) {
    drawCircle(
        color = color,
        radius = radius,
        center = Offset(cx, cy),
        style = androidx.compose.ui.graphics.drawscope.Stroke(width = radius * 0.025f)
    )
    val rings = 6
    for (i in 1..rings) {
        drawCircle(
            color = color.copy(alpha = 0.2f),
            radius = radius * (i.toFloat() / rings),
            center = Offset(cx, cy),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = radius * 0.012f)
        )
    }
}

private fun DrawScope.drawModernBlades(
    cx: Float, cy: Float, bladeRadius: Float,
    rotation: Float, bladeColor: Color, accentColor: Color
) {
    val bladeCount = 5
    for (i in 0 until bladeCount) {
        val baseAngle = rotation + i * (360f / bladeCount)
        rotate(baseAngle, pivot = Offset(cx, cy)) {
            val path = Path().apply {
                val halfW = bladeRadius * 0.10f
                moveTo(cx, cy)
                lineTo(cx - halfW, cy)
                lineTo(cx - halfW * 0.5f, cy - bladeRadius)
                lineTo(cx + halfW * 0.5f, cy - bladeRadius)
                lineTo(cx + halfW, cy)
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

private fun DrawScope.drawHub(cx: Float, cy: Float, radius: Float, color: Color) {
    drawCircle(color = color, radius = radius, center = Offset(cx, cy))
}

private fun DrawScope.drawPole(cx: Float, cy: Float, radius: Float, color: Color, slim: Boolean) {
    val poleWidth = if (slim) radius * 0.05f else radius * 0.08f
    drawRect(
        color = color,
        topLeft = Offset(cx - poleWidth / 2f, cy + radius * 0.9f),
        size = Size(poleWidth, radius * 0.5f)
    )
}

private fun DrawScope.drawBase(cx: Float, baseY: Float, color: Color) {
    drawRoundRect(
        color = color,
        topLeft = Offset(cx - 70f, baseY - 8f),
        size = Size(140f, 16f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(8f)
    )
}
