package com.majotyler.hiittimer.presentation.createWorkoutScreen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.majotyler.hiittimer.presentation.common.TableCard
import com.majotyler.hiittimer.presentation.common.expect.BackHandler
import com.majotyler.hiittimer.presentation.common.ui.HiitAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateWorkoutScreen(viewModel: WorkoutViewModel) {
    val nameWorkout by viewModel.nameWorkout.collectAsStateWithLifecycle()
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
    val page by viewModel.page.collectAsStateWithLifecycle()

    BackHandler(enabled = true) {
        viewModel.onEvent(event = WorkoutViewEvent.ClickedNavigateUp)
    }

    CreateWorkoutScreenContent(
        addExerciseButtonIsEnabled = false,
        bottomBarButtonIsEnabled = true,
        nameExercise = "",
        nameWorkout = nameWorkout,
        page = page,
        secondsDuration = 0,
        secondsRest = 0,
        onExerciseNameChanged = {},
        onWorkoutNameChanged = {
            viewModel.onEvent(event = WorkoutViewEvent.NameWorkout(newNameWorkout = it))
        },
        onDurationChanged = {},
        onRestChanged = {},
        onClickedAddExercise = {},
        onClickedBottomBarButton = {
            viewModel.onEvent(event = WorkoutViewEvent.ClickedAdvanceButton)
        },
        onClickedNavigateUp = {
            viewModel.onEvent(event = WorkoutViewEvent.ClickedNavigateUp)
        },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateWorkoutScreenContent(
    addExerciseButtonIsEnabled: Boolean,
    bottomBarButtonIsEnabled: Boolean,
    nameExercise: String,
    nameWorkout: String,
    page: CreateWorkoutPage,
    secondsDuration: Int,
    secondsRest: Int,
    onClickedAddExercise: () -> Unit,
    onExerciseNameChanged: (String) -> Unit,
    onWorkoutNameChanged: (String) -> Unit,
    onDurationChanged: (String) -> Unit,
    onRestChanged: (String) -> Unit,
    onClickedBottomBarButton: () -> Unit,
    onClickedNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Create Workout")
                },
                navigationIcon = {
                    IconButton(
                        onClick = onClickedNavigateUp,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Navigate up",
                        )
                    }
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                content = {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier.fillMaxWidth(),
                    ) {
                        Button(
                            enabled = bottomBarButtonIsEnabled,
                            onClick = onClickedBottomBarButton,
                        ) {
                            Text(text = page.actionButtonText)
                        }
                    }
                },
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = innerPadding)
        ) {
            HorizontalDivider()

            val pagerState = rememberPagerState(
                initialPage = remember { page.ordinal },
                pageCount = { CreateWorkoutPage.entries.size },
            )

            LaunchedEffect(page) {
                pagerState.animateScrollToPage(
                    page = page.ordinal,
                    animationSpec = tween(
                        durationMillis = 500,
                    )
                )
            }

            HorizontalPager(
                modifier = Modifier.fillMaxSize(),
                state = pagerState,
                userScrollEnabled = false,
            ) {
                val page = CreateWorkoutPage.entries.get(index = it)

                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                ) {
                    when (page) {
                        CreateWorkoutPage.ADD_EXERCISES -> CreateWorkoutAddExercisesPage(
                            addExerciseButtonIsEnabled = addExerciseButtonIsEnabled,
                            nameExercise = nameExercise,
                            secondsDuration = secondsDuration,
                            secondsRest = secondsRest,
                            onClickedAddExercise = onClickedAddExercise,
                            onExerciseNameChanged = onExerciseNameChanged,
                            onDurationChanged = onDurationChanged,
                            onRestChanged = onRestChanged,
                        )

                        CreateWorkoutPage.NAME_WORKOUT -> CreateWorkoutNameWorkoutPage(
                            nameWorkout = nameWorkout,
                            onWorkoutNameChanged = onWorkoutNameChanged,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun CreateWorkoutAddExercisesPage(
    addExerciseButtonIsEnabled: Boolean,
    nameExercise: String,
    secondsDuration: Int,
    secondsRest: Int,
    onClickedAddExercise: () -> Unit,
    onDurationChanged: (String) -> Unit,
    onRestChanged: (String) -> Unit,
    onExerciseNameChanged: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        Text(
            text = "Add Exercise",
            style = MaterialTheme.typography.titleLarge,
        )

        Text(
            text = "A Workout has at least one exercise. Each exercise has a duration, and a rest period that follows it.",
            style = MaterialTheme.typography.bodyMedium,
        )

        TextField(
            value = nameExercise,
            maxLines = 1,
            onValueChange = onExerciseNameChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Exercise Name")
            },
        )

        TextField(
            maxLines = 1,
            label = {
                Text(text = "Duration")
            },
            value = secondsDuration.toString(),
            onValueChange = onDurationChanged,
            modifier = Modifier.fillMaxWidth(),
            suffix = {
                Text(text = "seconds")
            },
        )

        TextField(
            maxLines = 1,
            label = {
                Text(text = "Rest")
            },
            value = secondsRest.toString(),
            onValueChange = onRestChanged,
            modifier = Modifier.fillMaxWidth(),
            suffix = {
                Text(text = "seconds")
            },
        )

        OutlinedButton(
            enabled = addExerciseButtonIsEnabled,
            modifier = Modifier.fillMaxWidth(),
            onClick = onClickedAddExercise,
        ) {
            Text(
                text = "Add Exercise to Workout",
            )
        }

        HorizontalDivider()

        TableCard(
            header = "Exercises in Workout",
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1F),
            content = {

            },
        )
    }

}

@Composable
private fun CreateWorkoutNameWorkoutPage(
    nameWorkout: String,
    onWorkoutNameChanged: (String) -> Unit,
) {
    Column(verticalArrangement = Arrangement.spacedBy(space = 8.dp)) {
        Text(
            text = "Name Workout",
            style = MaterialTheme.typography.titleLarge,
        )

        TextField(
            value = nameWorkout,
            maxLines = 1,
            onValueChange = onWorkoutNameChanged,
            modifier = Modifier.fillMaxWidth(),
            label = {
                Text(text = "Workout Name")
            },
        )


        HorizontalDivider()

        Text(
            text = "Workout Preview",
            style = MaterialTheme.typography.titleLarge,
        )

        TableCard(
            header = "Exercises in Workout",
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1F),
            content = {

            },
        )
    }
}

@Preview
@Composable
private fun CreateWorkoutScreenContent_Preview() {
    HiitAppTheme {
        CreateWorkoutScreenContent(
            addExerciseButtonIsEnabled = false,
            bottomBarButtonIsEnabled = true,
            nameExercise = "",
            nameWorkout = "",
            page = CreateWorkoutPage.ADD_EXERCISES,
            secondsDuration = 0,
            secondsRest = 0,
            onExerciseNameChanged = {},
            onDurationChanged = {},
            onRestChanged = {},
            onClickedBottomBarButton = {},
            onClickedNavigateUp = {},
            onClickedAddExercise = {},
            onWorkoutNameChanged = {},
        )
    }
}

@Preview
@Composable
private fun CreateWorkoutScreenContent_NameWorkout_Preview() {
    HiitAppTheme {
        CreateWorkoutScreenContent(
            addExerciseButtonIsEnabled = false,
            bottomBarButtonIsEnabled = true,
            nameExercise = "",
            nameWorkout = "",
            page = CreateWorkoutPage.NAME_WORKOUT,
            secondsDuration = 0,
            secondsRest = 0,
            onExerciseNameChanged = {},
            onDurationChanged = {},
            onRestChanged = {},
            onClickedBottomBarButton = {},
            onClickedNavigateUp = {},
            onClickedAddExercise = {},
            onWorkoutNameChanged = {},
        )
    }
}