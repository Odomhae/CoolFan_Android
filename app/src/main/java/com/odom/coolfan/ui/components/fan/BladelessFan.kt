package com.odom.coolfan.ui.components.fan

import androidx.compose.foundation.Canvas
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.odom.coolfan.model.ThemeColors

@Composable
fun BladelessFan(
    modifier: Modifier = Modifier,
    rotationAngle: Float,
    themeColors: ThemeColors
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height * 0.45f
        val outerR = minOf(size.width, size.height) * 0.40f
        val innerR = outerR * 0.60f

        drawRing(cx, cy, outerR, innerR, rotationAngle, themeColors)
        drawBase(cx, cy + outerR + 10f, outerR * 0.18f, outerR * 0.55f, themeColors.frame)
        drawPillar(cx, cy + outerR, outerR * 0.55f, themeColors.frame)
    }
}

private fun DrawScope.drawRing(
    cx: Float, cy: Float, outerR: Float, innerR: Float,
    rotation: Float, themeColors: ThemeColors
) {
    val ringThickness = (outerR - innerR)

    drawCircle(
        brush = Brush.sweepGradient(
            colors = listOf(
                themeColors.frame,
                themeColors.accent.copy(alpha = 0.7f),
                themeColors.frame,
                themeColors.accent.copy(alpha = 0.4f),
                themeColors.frame
            ),
            center = Offset(cx, cy)
        ),
        radius = (outerR + innerR) / 2f,
        center = Offset(cx, cy),
        style = Stroke(width = ringThickness)
    )

    rotate(rotation, pivot = Offset(cx, cy)) {
        val flowCount = 8
        for (i in 0 until flowCount) {
            val angle = (i * 360f / flowCount) * (Math.PI / 180f)
            val r = (outerR + innerR) / 2f
            val sx = cx + r * 0.85f * Math.cos(angle).toFloat()
            val sy = cy + r * 0.85f * Math.sin(angle).toFloat()
            val ex = cx + r * 1.15f * Math.cos(angle + 0.3).toFloat()
            val ey = cy + r * 1.15f * Math.sin(angle + 0.3).toFloat()
            drawLine(
                color = themeColors.accent.copy(alpha = 0.5f),
                start = Offset(sx, sy),
                end = Offset(ex, ey),
                strokeWidth = ringThickness * 0.15f
            )
        }
    }

    drawCircle(
        color = themeColors.accent,
        radius = outerR,
        center = Offset(cx, cy),
        style = Stroke(width = 3f)
    )
    drawCircle(
        color = themeColors.accent.copy(alpha = 0.4f),
        radius = innerR,
        center = Offset(cx, cy),
        style = Stroke(width = 2f)
    )
}

private fun DrawScope.drawPillar(cx: Float, startY: Float, height: Float, color: Color) {
    val w = 18f
    drawRect(
        color = color,
        topLeft = Offset(cx - w / 2f, startY),
        size = Size(w, height)
    )
}

private fun DrawScope.drawBase(cx: Float, baseY: Float, height: Float, width: Float, color: Color) {
    drawOval(
        color = color,
        topLeft = Offset(cx - width, baseY),
        size = Size(width * 2f, height)
    )
}
