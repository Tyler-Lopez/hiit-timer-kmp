package com.majotyler.hiittimer.presentation.playWorkoutScreen

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.LineHeightStyle
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun PlayWorkoutScreen(
    viewModel: PlayWorkoutVIewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val seconds = state.seconds
    val play by viewModel.play.collectAsStateWithLifecycle()
    val text by viewModel.text.collectAsStateWithLifecycle()
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()

    PlayWorkoutContent(
        seconds = seconds,
        onClickedPlay = { viewModel.onEvent(event = PlayWorkoutViewEvent.ClickedPlay) },
        text = text,
        play = play,
        onClickedPause = {
            viewModel.onEvent(
                PlayWorkoutViewEvent.ClickedPause
            )
        }, enabled, onEnabled = { viewModel.onEvent(PlayWorkoutViewEvent.Enabled) }
    )
}

@Composable
private fun PlayWorkoutContent(
    seconds: Int,
    onClickedPlay: () -> Unit,
    text: String,
    play: Boolean,
    onClickedPause: () -> Unit,
    enabled: Boolean,
    onEnabled: () -> Unit
) {
    val totalTime = 50
    val progress by animateFloatAsState(seconds / totalTime.toFloat())
    LaunchedEffect(progress) {
        if (progress >= 1f) {
            onClickedPause()
            onEnabled()

        }
    }
    Column(
        modifier = Modifier.fillMaxSize().background(color = Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            Modifier.size(200.dp).drawBehind {
                drawArc(
                    startAngle = 180F, sweepAngle = 360F, useCenter = false,
                    style = Stroke(width = 10f), color = Color.LightGray
                )

                drawArc(
                    startAngle = 180F,
                    sweepAngle = 360f * progress,
                    useCenter = false,
                    style = Stroke(width = 12f),
                    color = Color.Green
                )
            },
            contentAlignment = Alignment.Center
        ) {
            Text("$seconds")
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            onClickedPlay()
        }, enabled = enabled) {
            Text(text)
        }


    }

}
