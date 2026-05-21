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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import com.odom.coolfan.model.ThemeColors
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun CuteFan(
    modifier: Modifier = Modifier,
    rotationAngle: Float,
    themeColors: ThemeColors
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f
        val cy = size.height / 2f
        val radius = minOf(size.width, size.height) * 0.40f

        drawCuteBody(cx, cy, radius, themeColors)
        drawCuteBlades(cx, cy, radius * 0.65f, rotationAngle, themeColors)
        drawCuteFace(cx, cy, radius * 0.14f, themeColors.accent, themeColors.text)
        drawCutePole(cx, cy + radius * 0.9f, themeColors.frame)
        drawCuteBase(cx, cy + radius * 1.35f, themeColors.frame, themeColors.accent)
    }
}

private fun DrawScope.drawCuteBody(cx: Float, cy: Float, radius: Float, themeColors: ThemeColors) {
    drawCircle(
        brush = Brush.radialGradient(
            colors = listOf(
                themeColors.frame.copy(alpha = 0.6f),
                themeColors.frame.copy(alpha = 0.2f)
            ),
            center = Offset(cx, cy),
            radius = radius
        ),
        radius = radius,
        center = Offset(cx, cy)
    )
    drawCircle(
        color = themeColors.accent,
        radius = radius,
        center = Offset(cx, cy),
        style = Stroke(width = radius * 0.05f)
    )
    val dotCount = 8
    for (i in 0 until dotCount) {
        val angle = (i * 360f / dotCount) * Math.PI / 180f
        drawCircle(
            color = themeColors.accent.copy(alpha = 0.6f),
            radius = radius * 0.04f,
            center = Offset(
                cx + radius * 0.88f * cos(angle).toFloat(),
                cy + radius * 0.88f * sin(angle).toFloat()
            )
        )
    }
}

private fun DrawScope.drawCuteBlades(
    cx: Float, cy: Float, bladeRadius: Float,
    rotation: Float, themeColors: ThemeColors
) {
    val bladeCount = 4
    val colors = listOf(
        themeColors.accent,
        themeColors.blade,
        themeColors.accent.copy(alpha = 0.7f),
        themeColors.blade.copy(alpha = 0.8f)
    )
    for (i in 0 until bladeCount) {
        val baseAngle = rotation + i * (360f / bladeCount)
        rotate(baseAngle, pivot = Offset(cx, cy)) {
            val path = Path().apply {
                moveTo(cx, cy - bladeRadius * 0.15f)
                cubicTo(
                    cx + bladeRadius * 0.4f, cy - bladeRadius * 0.2f,
                    cx + bladeRadius * 0.5f, cy - bladeRadius * 0.9f,
                    cx, cy - bladeRadius
                )
                cubicTo(
                    cx - bladeRadius * 0.5f, cy - bladeRadius * 0.9f,
                    cx - bladeRadius * 0.4f, cy - bladeRadius * 0.2f,
                    cx, cy - bladeRadius * 0.15f
                )
                close()
            }
            drawPath(path = path, color = colors[i % colors.size])
        }
    }
}

private fun DrawScope.drawCuteFace(
    cx: Float, cy: Float, hubR: Float,
    accentColor: Color, textColor: Color
) {
    drawCircle(color = accentColor, radius = hubR, center = Offset(cx, cy))

    val eyeR = hubR * 0.22f
    val eyeOffX = hubR * 0.38f
    val eyeOffY = hubR * 0.15f
    drawCircle(color = textColor, radius = eyeR, center = Offset(cx - eyeOffX, cy - eyeOffY))
    drawCircle(color = textColor, radius = eyeR, center = Offset(cx + eyeOffX, cy - eyeOffY))

    drawCircle(
        color = Color.White,
        radius = eyeR * 0.4f,
        center = Offset(cx - eyeOffX + eyeR * 0.2f, cy - eyeOffY - eyeR * 0.2f)
    )
    drawCircle(
        color = Color.White,
        radius = eyeR * 0.4f,
        center = Offset(cx + eyeOffX + eyeR * 0.2f, cy - eyeOffY - eyeR * 0.2f)
    )

    val smilePath = Path().apply {
        moveTo(cx - hubR * 0.35f, cy + hubR * 0.15f)
        cubicTo(
            cx - hubR * 0.15f, cy + hubR * 0.5f,
            cx + hubR * 0.15f, cy + hubR * 0.5f,
            cx + hubR * 0.35f, cy + hubR * 0.15f
        )
    }
    drawPath(smilePath, color = textColor, style = Stroke(width = hubR * 0.12f))
}

private fun DrawScope.drawCutePole(cx: Float, poleTop: Float, color: Color) {
    drawRoundRect(
        color = color,
        topLeft = Offset(cx - 7f, poleTop),
        size = Size(14f, 50f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(7f)
    )
}

private fun DrawScope.drawCuteBase(cx: Float, baseY: Float, color: Color, accentColor: Color) {
    drawRoundRect(
        color = color,
        topLeft = Offset(cx - 55f, baseY - 12f),
        size = Size(110f, 24f),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f)
    )
    drawCircle(
        color = accentColor,
        radius = 6f,
        center = Offset(cx, baseY)
    )
}
