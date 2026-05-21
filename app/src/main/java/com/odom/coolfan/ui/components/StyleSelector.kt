package com.odom.coolfan.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.odom.coolfan.model.ButtonStyle
import com.odom.coolfan.model.ColorTheme
import com.odom.coolfan.model.FanStyle
import com.odom.coolfan.model.ThemeColors
import com.odom.coolfan.model.toThemeColors

private val tabs = listOf("Fan", "Button", "Theme")

@Composable
fun StyleSelector(
    fanStyle: FanStyle,
    buttonStyle: ButtonStyle,
    colorTheme: ColorTheme,
    themeColors: ThemeColors,
    onFanStyleChange: (FanStyle) -> Unit,
    onButtonStyleChange: (ButtonStyle) -> Unit,
    onColorThemeChange: (ColorTheme) -> Unit,
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }

    Column(modifier = modifier.fillMaxWidth().padding(horizontal = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            tabs.forEachIndexed { index, title ->
                val isActive = index == selectedTab
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isActive) themeColors.accent else themeColors.frame)
                        .clickable { selectedTab = index }
                        .padding(vertical = 8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        title,
                        color = if (isActive) themeColors.background else themeColors.text,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        when (selectedTab) {
            0 -> FanStyleRow(fanStyle, themeColors, onFanStyleChange)
            1 -> ButtonStyleRow(buttonStyle, themeColors, onButtonStyleChange)
            2 -> ColorThemeRow(colorTheme, themeColors, onColorThemeChange)
        }
    }
}

@Composable
private fun FanStyleRow(
    selected: FanStyle,
    themeColors: ThemeColors,
    onSelect: (FanStyle) -> Unit
) {
    val items = FanStyle.entries.map { it to it.label() }
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items) { (style, label) ->
            StyleChip(
                label = label,
                isSelected = style == selected,
                themeColors = themeColors,
                onClick = { onSelect(style) }
            )
        }
    }
}

@Composable
private fun ButtonStyleRow(
    selected: ButtonStyle,
    themeColors: ThemeColors,
    onSelect: (ButtonStyle) -> Unit
) {
    val items = ButtonStyle.entries.map { it to it.label() }
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(items) { (style, label) ->
            StyleChip(
                label = label,
                isSelected = style == selected,
                themeColors = themeColors,
                onClick = { onSelect(style) }
            )
        }
    }
}

@Composable
private fun ColorThemeRow(
    selected: ColorTheme,
    themeColors: ThemeColors,
    onSelect: (ColorTheme) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
        items(ColorTheme.entries) { theme ->
            val colors = theme.toThemeColors()
            ColorThemeChip(
                label = theme.label(),
                isSelected = theme == selected,
                accentColor = colors.accent,
                bgColor = colors.background,
                themeColors = themeColors,
                onClick = { onSelect(theme) }
            )
        }
    }
}

@Composable
private fun StyleChip(
    label: String,
    isSelected: Boolean,
    themeColors: ThemeColors,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) themeColors.accent else themeColors.frame)
            .border(
                width = if (isSelected) 0.dp else 1.dp,
                color = themeColors.accent.copy(alpha = 0.4f),
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            color = if (isSelected) themeColors.background else themeColors.text,
            fontSize = 13.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun ColorThemeChip(
    label: String,
    isSelected: Boolean,
    accentColor: Color,
    bgColor: Color,
    themeColors: ThemeColors,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) themeColors.accent.copy(alpha = 0.15f) else Color.Transparent)
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = if (isSelected) themeColors.accent else themeColors.frame,
                shape = RoundedCornerShape(12.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp, vertical = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(CircleShape)
                .background(bgColor)
                .border(2.dp, accentColor, CircleShape)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(label, color = themeColors.text, fontSize = 11.sp)
    }
}

private fun FanStyle.label() = when (this) {
    FanStyle.VINTAGE -> "Vintage"
    FanStyle.MODERN -> "Modern"
    FanStyle.BLADELESS -> "Dyson"
    FanStyle.CUTE -> "Cute"
}

private fun ButtonStyle.label() = when (this) {
    ButtonStyle.ROUND_DIAL -> "Dial"
    ButtonStyle.NEON -> "Neon"
    ButtonStyle.MINIMAL -> "Minimal"
    ButtonStyle.PHYSICAL -> "Physical"
}

private fun ColorTheme.label() = when (this) {
    ColorTheme.DARK_NEON -> "Neon"
    ColorTheme.PASTEL -> "Pastel"
    ColorTheme.METAL -> "Metal"
    ColorTheme.WOOD -> "Wood"
}
