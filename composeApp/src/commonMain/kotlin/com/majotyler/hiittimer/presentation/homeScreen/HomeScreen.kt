package com.majotyler.hiittimer.presentation.homeScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.majotyler.hiittimer.platform.UrlOpener
import hiittimer.composeapp.generated.resources.Res
import hiittimer.composeapp.generated.resources.btn_strava_connect_with_orange
import hiittimer.composeapp.generated.resources.home_button_label_launch_build_workouts
import hiittimer.composeapp.generated.resources.mineral_chico
import hiittimer.composeapp.generated.resources.rock_salt
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    urlOpener: UrlOpener,
    viewModel: HomeViewModel,
    modifier: Modifier = Modifier,
) {
    val showCreateActivityButton by viewModel.showCreateActivityButton.collectAsState()
    val rockSalt = FontFamily(Font(Res.font.rock_salt))

    LaunchedEffect(Unit) {
        viewModel.openUrl.collect { url ->
            urlOpener.openUrl(url = url)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        Image(
            painter = painterResource(Res.drawable.mineral_chico),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Text(
            text = "Track\nImprove",
            fontFamily = rockSalt,
            fontSize = 65.sp,
            color = Color.Black,
            lineHeight = 52.sp,
            modifier = Modifier
                .align(Alignment.Center)
                .padding(bottom = 180.dp),
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(horizontal = 24.dp, vertical = 48.dp)
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(
                onClick = { viewModel.onEvent(event = HomeViewEvent.ClickedLaunchBuildWorkouts) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black,
                ),
            ) {
                Text(
                    text = stringResource(resource = Res.string.home_button_label_launch_build_workouts),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            Image(
                painter = painterResource(Res.drawable.btn_strava_connect_with_orange),
                contentDescription = "Connect with Strava",
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .padding(20.dp)
                    .height(48.dp)
                    .widthIn(min = 48.dp)
                    .clip(RoundedCornerShape(6.dp))
                    .clickable { viewModel.onEvent(event = HomeViewEvent.ClickedConnectWithStrava) },
            )

            if (showCreateActivityButton) {
                Button(
                    onClick = { viewModel.onEvent(event = HomeViewEvent.ClickedCreateStravaActivity) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(28.dp),
                ) {
                    Text(text = "Create Strava Activity")
                }
            }
        }
    }
}
