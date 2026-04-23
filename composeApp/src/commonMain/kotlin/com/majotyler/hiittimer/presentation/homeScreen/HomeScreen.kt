package com.majotyler.hiittimer.presentation.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import com.majotyler.hiittimer.platform.UrlOpener
import hiittimer.composeapp.generated.resources.Res
import hiittimer.composeapp.generated.resources.home_button_label_connect_with_strava
import hiittimer.composeapp.generated.resources.home_button_label_launch_build_workouts
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    urlOpener: UrlOpener,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {
    LaunchedEffect(Unit) {
        viewModel.openUrl.collect { url ->
            urlOpener.openUrl(url = url)
        }
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                viewModel.onEvent(event = HomeViewEvent.ClickedLaunchBuildWorkouts)
            },
        ) {
            Text(text = stringResource(resource = Res.string.home_button_label_launch_build_workouts))
        }
        Button(
            onClick = {
                viewModel.onEvent(event = HomeViewEvent.ClickedConnectWithStrava)
            },
        ) {
            Text(text = stringResource(resource = Res.string.home_button_label_connect_with_strava))
        }

        if (viewModel.showCreateActivityButton) {
            Button(
                onClick = {
                    viewModel.onEvent(event = HomeViewEvent.ClickedCreateStravaActivity)
                }
            ) {
                Text(text = "Create Strava Activity")
            }
        }
    }
}