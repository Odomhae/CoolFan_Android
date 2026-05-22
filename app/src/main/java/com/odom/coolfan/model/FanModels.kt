package com.odom.coolfan.model

import androidx.compose.ui.graphics.Color

enum class FanStyle { VINTAGE, MODERN, BLADELESS, CUTE }
enum class ButtonStyle { ROUND_DIAL, NEON, MINIMAL, PHYSICAL }
enum class ColorTheme { DARK_NEON, PASTEL, METAL, WOOD, COOL_BLUE, SKY }

enum class FanSpeed { OFF, LOW, MEDIUM, HIGH }

data class FanState(
    val speed: FanSpeed = FanSpeed.OFF,
    val swinging: Boolean = false,
    val fanStyle: FanStyle = FanStyle.VINTAGE,
    val buttonStyle: ButtonStyle = ButtonStyle.ROUND_DIAL,
    val colorTheme: ColorTheme = ColorTheme.SKY,
    val selectedStyleTab: Int = 0
)

data class ThemeColors(
    val background: Color,
    val frame: Color,
    val blade: Color,
    val accent: Color,
    val text: Color
)

fun ColorTheme.toThemeColors(): ThemeColors = when (this) {
    ColorTheme.DARK_NEON -> ThemeColors(
        background = Color(0xFF0D0D0D),
        frame = Color(0xFF1A1A2E),
        blade = Color(0xFF16213E),
        accent = Color(0xFF00FFFF),
        text = Color(0xFFE0E0E0)
    )
    ColorTheme.PASTEL -> ThemeColors(
        background = Color(0xFFFFF0F5),
        frame = Color(0xFFE8D5E8),
        blade = Color(0xFFB8E0D2),
        accent = Color(0xFFFF8FAB),
        text = Color(0xFF5C4A6E)
    )
    ColorTheme.METAL -> ThemeColors(
        background = Color(0xFF2C2C2C),
        frame = Color(0xFF4A4A4A),
        blade = Color(0xFF6E6E6E),
        accent = Color(0xFFB0B0B0),
        text = Color(0xFFE8E8E8)
    )
    ColorTheme.WOOD -> ThemeColors(
        background = Color(0xFFF5E6D0),
        frame = Color(0xFFD4A574),
        blade = Color(0xFFC8956C),
        accent = Color(0xFF8B5E3C),
        text = Color(0xFF4A2C0A)
    )
    ColorTheme.COOL_BLUE -> ThemeColors(
        background = Color(0xFF071B35),
        frame = Color(0xFF0D3B6E),
        blade = Color(0xFF1565C0),
        accent = Color(0xFF29B6F6),
        text = Color(0xFFB3E5FC)
    )
    ColorTheme.SKY -> ThemeColors(
        background = Color(0xFFEBF8FF),
        frame = Color(0xFFBEE3F8),
        blade = Color(0xFF63B3ED),
        accent = Color(0xFF3182CE),
        text = Color(0xFF1A365D)
    )
}
