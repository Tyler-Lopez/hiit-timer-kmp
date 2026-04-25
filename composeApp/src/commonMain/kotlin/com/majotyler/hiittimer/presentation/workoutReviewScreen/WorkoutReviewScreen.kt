package com.majotyler.hiittimer.presentation.workoutReviewScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.majotyler.hiittimer.platform.UrlOpener

@Composable
fun WorkoutReviewScreen(
    urlOpener: UrlOpener,
    viewModel: WorkoutReviewViewModel,
) {
    val showCreateActivityButton by viewModel.showCreateActivityButton.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.openUrl.collect { url ->
            urlOpener.openUrl(url = url)
        }
    }

    WorkoutReviewContent(
        showCreateActivityButton = showCreateActivityButton,
        onClickedConnectWithStrava = {
            viewModel.onEvent(WorkoutReviewViewEvent.ClickedConnectWithStrava)
        },
        onClickedCreateStravaActivity = {
            viewModel.onEvent(WorkoutReviewViewEvent.ClickedCreateStravaActivity)
        },
    )
}

@Composable
private fun WorkoutReviewContent(
    showCreateActivityButton: Boolean,
    onClickedConnectWithStrava: () -> Unit,
    onClickedCreateStravaActivity: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = "Workout Complete",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.primary,
        )
        if (showCreateActivityButton) {
            Button(onClick = onClickedCreateStravaActivity) {
                Text(text = "Create Strava Activity")
            }
        } else {
            Button(onClick = onClickedConnectWithStrava) {
                Text(text = "Connect with Strava")
            }
        }
    }
}
