package com.majotyler.hiittimer.presentation.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import com.majotyler.hiittimer.platform.UrlOpener
import hiittimer.composeapp.generated.resources.Res
import hiittimer.composeapp.generated.resources.btn_strava_connect_with_orange
import hiittimer.composeapp.generated.resources.home_button_label_launch_build_workouts
import org.jetbrains.compose.resources.painterResource
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
        Image(
            painter = painterResource(Res.drawable.btn_strava_connect_with_orange),
            contentDescription = "Connect with Strava",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .height(48.dp)
                .widthIn(min = 48.dp)
                .clip(RoundedCornerShape(6.dp))
                .clickable { viewModel.onEvent(event = HomeViewEvent.ClickedConnectWithStrava) },
        )
    }
}
