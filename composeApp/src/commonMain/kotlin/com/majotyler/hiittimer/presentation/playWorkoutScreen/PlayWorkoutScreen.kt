package com.majotyler.hiittimer.presentation.playWorkoutScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.majotyler.hiittimer.presentation.common.composables.ConfirmationDialog
import com.majotyler.hiittimer.presentation.common.expect.BackHandler

@Composable
fun PlayWorkoutScreen(
    viewModel: PlayWorkoutVIewModel,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val play by viewModel.play.collectAsStateWithLifecycle()
    val text by viewModel.text.collectAsStateWithLifecycle()
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()

    BackHandler(
        onBack = {
            viewModel.onEvent(event = PlayWorkoutViewEvent.ClickedSystemBack)
        }
    )

    PlayWorkoutContent(
        confirmationDialogVisible = state.confirmationDialogVisible,
        progressDisplay = state.progressDisplay,
        progress = state.progress,
        onClickedDialogCancel = {
            viewModel.onEvent(event = PlayWorkoutViewEvent.ClickedDialogCancel)
        },
        onClickedDialogConfirm = {
            viewModel.onEvent(event = PlayWorkoutViewEvent.ClickedDialogConfirm)
        },
        onClickedPlay = { viewModel.onEvent(event = PlayWorkoutViewEvent.ClickedPlay) },
        text = text,
        play = play,
        onClickedPause = {
            viewModel.onEvent(
                PlayWorkoutViewEvent.ClickedPause
            )
        },
        enabled = enabled,
    )
}

@Composable
private fun PlayWorkoutContent(
    confirmationDialogVisible: Boolean,
    progressDisplay: String,
    progress: Float,
    onClickedPlay: () -> Unit,
    text: String,
    play: Boolean,
    onClickedDialogConfirm: () -> Unit,
    onClickedDialogCancel: () -> Unit,
    onClickedPause: () -> Unit,
    enabled: Boolean,
) {
    if (confirmationDialogVisible) {
        ConfirmationDialog(
            text = "Are you sure you want to stop your workout?",
            onClickPositive = onClickedDialogConfirm,
            onClickNegative = onClickedDialogCancel,
            onDismiss = onClickedDialogCancel,
        )
    }

    Column(
        modifier = Modifier.fillMaxSize().background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        val progressTrackColor = MaterialTheme.colorScheme.secondary

        Box(
            Modifier.size(200.dp).drawBehind {
                drawArc(
                    startAngle = 180F, sweepAngle = 360F, useCenter = false,
                    style = Stroke(width = 10f), color = progressTrackColor,
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
            Text(
                style = MaterialTheme.typography.titleLarge,
                text = progressDisplay, color = MaterialTheme.colorScheme.primary
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Button(onClick = {
            if (play) {
                onClickedPause()
            } else {
                onClickedPlay()
            }
        }, enabled = enabled) {
            Text(text)
        }


    }

}
