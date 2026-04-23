package com.majotyler.hiittimer.presentation.common.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import hiittimer.composeapp.generated.resources.Res
import hiittimer.composeapp.generated.resources.build_workouts_interval_chip_duration
import hiittimer.composeapp.generated.resources.build_workouts_interval_chip_rest
import org.jetbrains.compose.resources.stringResource

private val WorkGreen = Color(0xFF4CAF50)
private val RestYellow = Color(0xFFFFCC00)

@Composable
fun DurationLabel(seconds: Int) {
    Label(
        text = stringResource(
            resource = Res.string.build_workouts_interval_chip_duration,
            formatArgs = arrayOf(seconds),
        ),
        color = WorkGreen,
    )
}

@Composable
fun RestLabel(seconds: Int) {
    Label(
        text = stringResource(
            resource = Res.string.build_workouts_interval_chip_rest,
            formatArgs = arrayOf(seconds),
        ),
        color = RestYellow,
    )
}

@Composable
private fun Label(text: String, color: Color) {
    val shape = RoundedCornerShape(6.dp)
    Box(
        modifier = Modifier
            .background(color = color.copy(alpha = 0.15f), shape = shape)
            .border(width = 1.dp, color = color, shape = shape)
            .padding(horizontal = 8.dp, vertical = 3.dp),
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.labelSmall,
            color = color,
            fontWeight = FontWeight.SemiBold,
        )
    }
}
