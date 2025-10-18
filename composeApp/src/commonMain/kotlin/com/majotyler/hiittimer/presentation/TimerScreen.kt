package com.majotyler.hiittimer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun TimerScreen(
    viewModel: TimerViewModel,
) {
    val state = viewModel.state.collectAsStateWithLifecycle()

    val seconds = state.value.seconds

    TimerScreenContent(
        seconds = seconds,
        onEvent = { event -> viewModel.onEvent(event = event) },
    )
}

@Composable
private fun TimerScreenContent(
    seconds: Int,
    onEvent: (TimerViewEvent) -> Unit,
) {
    /** TODO (Majo):
     * Create a simple screen with a button to start a timer, to stop a timer, and text which will
     * read how long the timer has been running.
     */
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background)
            .safeDrawingPadding(),
    ) {
        Text(text = "Placeholder text.")
    }
}

//region Previews
@Preview
@Composable
private fun TimerScreenContent_Preview_Zero_Seconds() {
    TimerScreenContent(
        seconds = 0,
        onEvent = {},
    )
}
//endregion Previews