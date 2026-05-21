package com.odom.coolfan.ui.components.fan

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
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
fun CuteFanHead(
    modifier: Modifier = Modifier,
    rotationAngle: Float,
    themeColors: ThemeColors
) {
    val bladeYTilt = sin(rotationAngle * PI / 180.0).toFloat() * 35f

    Box(modifier = modifier) {
        // Body circle (grill equivalent)
        Canvas(Modifier.fillMaxSize()) {
            val cx = size.width / 2f; val cy = size.height / 2f
            val r = minOf(size.width, size.height) * 0.40f
            drawCuteBody(cx, cy, r, themeColors)
        }
        // Blades with Y-tilt
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
                val r = minOf(size.width, size.height) * 0.40f
                drawCuteBlades(cx, cy, r * 0.65f, rotationAngle, themeColors)
            }
        }
        // Face — always on top, unaffected by blade tilt
        Canvas(Modifier.fillMaxSize()) {
            val cx = size.width / 2f; val cy = size.height / 2f
            val r = minOf(size.width, size.height) * 0.40f
            drawCuteFace(cx, cy, r * 0.14f, themeColors.accent, themeColors.text)
        }
    }
}

@Composable
fun CuteFanBody(
    modifier: Modifier = Modifier,
    themeColors: ThemeColors
) {
    Canvas(modifier = modifier) {
        val cx = size.width / 2f; val cy = size.height / 2f
        val r = minOf(size.width, size.height) * 0.40f
        drawCutePole(cx, cy + r * 0.9f, themeColors.frame)
        drawCuteBase(cx, cy + r * 1.35f, themeColors.frame, themeColors.accent)
    }
}

private fun DrawScope.drawCuteBody(cx: Float, cy: Float, r: Float, themeColors: ThemeColors) {
    drawCircle(
        brush = Brush.radialGradient(listOf(themeColors.frame.copy(alpha = 0.6f), themeColors.frame.copy(alpha = 0.2f)), Offset(cx, cy), r),
        radius = r, center = Offset(cx, cy)
    )
    drawCircle(color = themeColors.accent, radius = r, center = Offset(cx, cy), style = Stroke(width = r * 0.05f))
    for (i in 0 until 8) {
        val a = (i * 45.0) * PI / 180.0
        drawCircle(color = themeColors.accent.copy(alpha = 0.6f), radius = r * 0.04f, center = Offset(cx + r * 0.88f * cos(a).toFloat(), cy + r * 0.88f * sin(a).toFloat()))
    }
}

private fun DrawScope.drawCuteBlades(cx: Float, cy: Float, bladeR: Float, rotation: Float, themeColors: ThemeColors) {
    val colors = listOf(themeColors.accent, themeColors.blade, themeColors.accent.copy(alpha = 0.7f), themeColors.blade.copy(alpha = 0.8f))
    for (i in 0 until 4) {
        rotate(rotation + i * 90f, pivot = Offset(cx, cy)) {
            val path = Path().apply {
                moveTo(cx, cy - bladeR * 0.15f)
                cubicTo(cx + bladeR * 0.4f, cy - bladeR * 0.2f, cx + bladeR * 0.5f, cy - bladeR * 0.9f, cx, cy - bladeR)
                cubicTo(cx - bladeR * 0.5f, cy - bladeR * 0.9f, cx - bladeR * 0.4f, cy - bladeR * 0.2f, cx, cy - bladeR * 0.15f)
                close()
            }
            drawPath(path = path, color = colors[i % colors.size])
        }
    }
}

private fun DrawScope.drawCuteFace(cx: Float, cy: Float, hubR: Float, accentColor: Color, textColor: Color) {
    drawCircle(color = accentColor, radius = hubR, center = Offset(cx, cy))
    val eyeR = hubR * 0.22f; val ex = hubR * 0.38f; val ey = hubR * 0.15f
    drawCircle(color = textColor, radius = eyeR, center = Offset(cx - ex, cy - ey))
    drawCircle(color = textColor, radius = eyeR, center = Offset(cx + ex, cy - ey))
    drawCircle(color = Color.White, radius = eyeR * 0.4f, center = Offset(cx - ex + eyeR * 0.2f, cy - ey - eyeR * 0.2f))
    drawCircle(color = Color.White, radius = eyeR * 0.4f, center = Offset(cx + ex + eyeR * 0.2f, cy - ey - eyeR * 0.2f))
    val smile = Path().apply {
        moveTo(cx - hubR * 0.35f, cy + hubR * 0.15f)
        cubicTo(cx - hubR * 0.15f, cy + hubR * 0.5f, cx + hubR * 0.15f, cy + hubR * 0.5f, cx + hubR * 0.35f, cy + hubR * 0.15f)
    }
    drawPath(smile, color = textColor, style = Stroke(width = hubR * 0.12f))
}

private fun DrawScope.drawCutePole(cx: Float, poleTop: Float, color: Color) {
    drawRoundRect(color = color, topLeft = Offset(cx - 7f, poleTop), size = Size(14f, 50f), cornerRadius = CornerRadius(7f))
}

private fun DrawScope.drawCuteBase(cx: Float, baseY: Float, color: Color, accentColor: Color) {
    drawRoundRect(color = color, topLeft = Offset(cx - 55f, baseY - 12f), size = Size(110f, 24f), cornerRadius = CornerRadius(12f))
    drawCircle(color = accentColor, radius = 6f, center = Offset(cx, baseY))
}
