package com.odom.coolfan.ui

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.odom.coolfan.model.ButtonStyle
import com.odom.coolfan.model.ColorTheme
import com.odom.coolfan.model.FanSpeed
import com.odom.coolfan.model.FanState
import com.odom.coolfan.model.FanStyle
import com.odom.coolfan.model.toThemeColors
import com.odom.coolfan.ui.components.ControlPanel
import com.odom.coolfan.ui.components.FanCanvas
import com.odom.coolfan.ui.components.StyleSelector

@Composable
fun FanAppScreen() {
    var fanState by remember { mutableStateOf(FanState()) }
    val themeColors = fanState.colorTheme.toThemeColors()

    Crossfade(
        targetState = themeColors,
        animationSpec = tween(400),
        label = "theme_crossfade"
    ) { colors ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(colors.background)
                .statusBarsPadding()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Cool Fan",
                color = colors.accent,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 16.dp)
            )

            FanCanvas(
                fanState = fanState,
                themeColors = colors,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            HorizontalDivider(color = colors.frame, thickness = 1.dp)

            Spacer(modifier = Modifier.height(8.dp))

            ControlPanel(
                speed = fanState.speed,
                swinging = fanState.swinging,
                buttonStyle = fanState.buttonStyle,
                themeColors = colors,
                onSpeedChange = { fanState = fanState.copy(speed = it) },
                onSwingToggle = { fanState = fanState.copy(swinging = !fanState.swinging) }
            )

            Spacer(modifier = Modifier.height(8.dp))

            HorizontalDivider(color = colors.frame, thickness = 1.dp)

            Spacer(modifier = Modifier.height(12.dp))

            StyleSelector(
                fanStyle = fanState.fanStyle,
                buttonStyle = fanState.buttonStyle,
                colorTheme = fanState.colorTheme,
                themeColors = colors,
                onFanStyleChange = { fanState = fanState.copy(fanStyle = it) },
                onButtonStyleChange = { fanState = fanState.copy(buttonStyle = it) },
                onColorThemeChange = { fanState = fanState.copy(colorTheme = it) }
            )

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
