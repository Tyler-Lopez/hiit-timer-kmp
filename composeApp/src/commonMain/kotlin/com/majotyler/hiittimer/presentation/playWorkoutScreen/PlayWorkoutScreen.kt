package com.majotyler.hiittimer.presentation.playWorkoutScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PlayWorkoutScreen(
    viewModel: PlayWorkoutVIewModel,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val seconds = state.value.seconds

    PlayWorkoutContent(
        seconds = seconds,
        onEvent = { event -> viewModel.onEvent(event = event) },
    )
}

@Composable
private fun PlayWorkoutContent(
    seconds: Int,
    onEvent: (PlayWorkoutViewEvent) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .safeDrawingPadding(), contentAlignment = Alignment.Center
    ) {
        Text(text = "Placeholder text.")
    }
}

//region Previews
@Preview
@Composable
private fun PlayWorkoutContent_Preview_Zero_Seconds() {
    PlayWorkoutContent(
        seconds = 0,
        onEvent = {},
    )
}
//endregion Previews