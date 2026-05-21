package com.odom.coolfan.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.odom.coolfan.model.ButtonStyle
import com.odom.coolfan.model.ThemeColors

@Composable
fun CustomButton(
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    buttonStyle: ButtonStyle,
    themeColors: ThemeColors,
    modifier: Modifier = Modifier,
    size: Dp = 56.dp
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.90f else 1f,
        animationSpec = tween(100),
        label = "btn_scale"
    )

    when (buttonStyle) {
        ButtonStyle.ROUND_DIAL -> RoundDialButton(
            label, isSelected, onClick, interactionSource, scale, themeColors, modifier, size
        )
        ButtonStyle.NEON -> NeonButton(
            label, isSelected, onClick, interactionSource, scale, themeColors, modifier, size
        )
        ButtonStyle.MINIMAL -> MinimalButton(
            label, isSelected, onClick, interactionSource, scale, themeColors, modifier, size
        )
        ButtonStyle.PHYSICAL -> PhysicalButton(
            label, isSelected, onClick, interactionSource, scale, themeColors, modifier, size
        )
    }
}

@Composable
private fun RoundDialButton(
    label: String, isSelected: Boolean, onClick: () -> Unit,
    interactionSource: MutableInteractionSource, scale: Float,
    themeColors: ThemeColors, modifier: Modifier, size: Dp
) {
    val bgColor = if (isSelected) themeColors.accent else themeColors.frame
    val textColor = if (isSelected) themeColors.background else themeColors.text

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .size(size)
            .shadow(if (isSelected) 8.dp else 3.dp, CircleShape)
            .clip(CircleShape)
            .background(bgColor)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = textColor, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun NeonButton(
    label: String, isSelected: Boolean, onClick: () -> Unit,
    interactionSource: MutableInteractionSource, scale: Float,
    themeColors: ThemeColors, modifier: Modifier, size: Dp
) {
    val glowColor = themeColors.accent
    val borderColor = if (isSelected) glowColor else glowColor.copy(alpha = 0.4f)
    val bgBrush = if (isSelected) {
        Brush.radialGradient(listOf(glowColor.copy(alpha = 0.3f), themeColors.background))
    } else {
        Brush.radialGradient(listOf(themeColors.background, themeColors.background))
    }

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .size(size)
            .clip(CircleShape)
            .background(bgBrush)
            .border(if (isSelected) 2.dp else 1.dp, borderColor, CircleShape)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = if (isSelected) glowColor else glowColor.copy(alpha = 0.7f),
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun MinimalButton(
    label: String, isSelected: Boolean, onClick: () -> Unit,
    interactionSource: MutableInteractionSource, scale: Float,
    themeColors: ThemeColors, modifier: Modifier, size: Dp
) {
    val shape = RoundedCornerShape(8.dp)
    val bgColor = if (isSelected) themeColors.accent.copy(alpha = 0.2f) else Color.Transparent
    val borderColor = if (isSelected) themeColors.accent else themeColors.text.copy(alpha = 0.3f)
    val textColor = if (isSelected) themeColors.accent else themeColors.text.copy(alpha = 0.7f)

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .size(width = size * 1.3f, height = size * 0.65f)
            .clip(shape)
            .background(bgColor)
            .border(1.dp, borderColor, shape)
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(label, color = textColor, fontSize = 12.sp, fontWeight = FontWeight.Medium)
    }
}

@Composable
private fun PhysicalButton(
    label: String, isSelected: Boolean, onClick: () -> Unit,
    interactionSource: MutableInteractionSource, scale: Float,
    themeColors: ThemeColors, modifier: Modifier, size: Dp
) {
    val isPressed by interactionSource.collectIsPressedAsState()
    val elevation = if (isPressed || isSelected) 1.dp else 6.dp
    val bgColor = if (isSelected) themeColors.accent else themeColors.frame

    Box(
        modifier = modifier
            .graphicsLayer { scaleX = scale; scaleY = scale }
            .size(size)
            .shadow(elevation, CircleShape)
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    listOf(
                        bgColor.copy(alpha = 0.9f),
                        bgColor.copy(alpha = 0.6f)
                    )
                )
            )
            .clickable(interactionSource = interactionSource, indication = null, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = if (isSelected) themeColors.background else themeColors.text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold
        )
    }
}
