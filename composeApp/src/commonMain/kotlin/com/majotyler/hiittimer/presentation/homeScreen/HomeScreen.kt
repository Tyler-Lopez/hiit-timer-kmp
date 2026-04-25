package com.majotyler.hiittimer.presentation.homeScreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import hiittimer.composeapp.generated.resources.Res
import hiittimer.composeapp.generated.resources.home_button_label_launch_build_workouts
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Button(
            onClick = {
                viewModel.onEvent(event = HomeViewEvent.ClickedLaunchBuildWorkouts)
            },
        ) {
            Text(text = stringResource(resource = Res.string.home_button_label_launch_build_workouts))
        }
    }
}
