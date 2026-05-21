package com.odom.coolfan.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.odom.coolfan.model.ButtonStyle
import com.odom.coolfan.model.FanSpeed
import com.odom.coolfan.model.ThemeColors

@Composable
fun ControlPanel(
    speed: FanSpeed,
    swinging: Boolean,
    buttonStyle: ButtonStyle,
    themeColors: ThemeColors,
    onSpeedChange: (FanSpeed) -> Unit,
    onSwingToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Speed",
                color = themeColors.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(end = 4.dp)
            )
            FanSpeed.entries.forEach { s ->
                val label = when (s) {
                    FanSpeed.OFF -> "Off"
                    FanSpeed.LOW -> "1"
                    FanSpeed.MEDIUM -> "2"
                    FanSpeed.HIGH -> "3"
                }
                CustomButton(
                    label = label,
                    isSelected = speed == s,
                    onClick = { onSpeedChange(s) },
                    buttonStyle = buttonStyle,
                    themeColors = themeColors,
                    size = 48.dp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Swing",
                color = themeColors.text,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold
            )
            Switch(
                checked = swinging,
                onCheckedChange = { onSwingToggle() },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = themeColors.background,
                    checkedTrackColor = themeColors.accent,
                    uncheckedThumbColor = themeColors.text.copy(alpha = 0.6f),
                    uncheckedTrackColor = themeColors.frame
                )
            )
        }
    }
}
