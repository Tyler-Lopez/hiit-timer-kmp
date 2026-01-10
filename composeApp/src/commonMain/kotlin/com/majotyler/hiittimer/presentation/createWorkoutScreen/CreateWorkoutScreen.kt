package com.majotyler.hiittimer.presentation.createWorkoutScreen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.common.TableCard
import com.majotyler.hiittimer.presentation.common.ui.HiitAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateWorkoutScreen(
    viewModel: WorkoutViewModel,
) {
    val nameWorkout by viewModel.nameWorkout.collectAsStateWithLifecycle()
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
    val page by viewModel.page.collectAsStateWithLifecycle()

    CreateWorkoutScreenContent(
        addExerciseButtonIsEnabled = false,
        bottomBarButtonIsEnabled = true,
        nameExercise = "",
        nameWorkout = nameWorkout,
        repetitions = 1,
        page = page,
        secondsDuration = 0,
        secondsRest = 0,
        onExerciseNameChanged = {},
        onWorkoutNameChanged = {
            viewModel.onEvent(event = WorkoutViewEvent.NameWorkout(newNameWorkout = it))
        },
        onDurationChanged = {},
        onRepetitionsDecreased = {},
        onRepetitionsIncreased = {},
        onRestChanged = {},
        onClickedAddExercise = {},
        onClickedBottomBarButton = {
            viewModel.onEvent(event = WorkoutViewEvent.ClickedAdvance)
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
    repetitions: Int,
    page: AddWorkoutPage,
    secondsDuration: Int,
    secondsRest: Int,
    onClickedAddExercise: () -> Unit,
    onExerciseNameChanged: (String) -> Unit,
    onWorkoutNameChanged: (String) -> Unit,
    onDurationChanged: (String) -> Unit,
    onRepetitionsDecreased: () -> Unit,
    onRepetitionsIncreased: () -> Unit,
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
                            Text(
                                text = if (page.ordinal == AddWorkoutPage.entries.lastIndex) {
                                    "Create and Add Workout"
                                } else {
                                    "Next"
                                },
                            )
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
                initialPage = page.ordinal,
                pageCount = { AddWorkoutPage.entries.size },
            )

            LaunchedEffect(page) {
                pagerState.animateScrollToPage(
                    page = page.ordinal,
                    animationSpec = tween(durationMillis = 500),
                )
            }

            HorizontalPager(
                userScrollEnabled = false,
                state = pagerState,
            ) { pageOrdinal ->

                val page = AddWorkoutPage.entries.get(index = pageOrdinal)

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 24.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(space = 8.dp),
                ) {
                    when (page) {
                        AddWorkoutPage.ADD_EXERCISES -> CreateWorkoutPageAddExercises(
                            addExerciseButtonIsEnabled = addExerciseButtonIsEnabled,
                            nameExercise = nameExercise,
                            secondsDuration = secondsDuration,
                            secondsRest = secondsRest,
                            onClickedAddExercise = onClickedAddExercise,
                            onDurationChanged = onDurationChanged,
                            onRestChanged = onRestChanged,
                            onExerciseNameChanged = onExerciseNameChanged,
                        )

                        AddWorkoutPage.NAME_WORKOUT -> CreateWorkoutPageNameWorkout(
                            nameWorkout = nameWorkout,
                            onWorkoutNameChanged = onWorkoutNameChanged,
                        )

                        AddWorkoutPage.SELECT_REPS -> CreateWorkoutPageRepetitions(
                            numberOfReps = repetitions,
                            onRepetitionsDecreased = onRepetitionsDecreased,
                            onRepetitionsIncreased = onRepetitionsIncreased,
                        )
                    }
                }
            }
        }
    }
}

//region Pages
@Composable
private fun ColumnScope.CreateWorkoutPageAddExercises(
    nameExercise: String,
    secondsDuration: Int,
    secondsRest: Int,
    addExerciseButtonIsEnabled: Boolean,
    onClickedAddExercise: () -> Unit,
    onDurationChanged: (String) -> Unit,
    onRestChanged: (String) -> Unit,
    onExerciseNameChanged: (String) -> Unit,
) {
    Text(
        text = "Add Exercises",
        style = MaterialTheme.typography.titleLarge,
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

    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        TextField(
            maxLines = 1,
            label = {
                Text(text = "Duration")
            },
            value = secondsDuration.toString(),
            onValueChange = onDurationChanged,
            modifier = Modifier.weight(weight = 1F),
            suffix = {
                Text(text = "s")
            },
        )

        TextField(
            maxLines = 1,
            label = {
                Text(text = "Rest")
            },
            value = secondsRest.toString(),
            onValueChange = onRestChanged,
            modifier = Modifier.weight(weight = 1F),
            suffix = {
                Text(text = "s")
            },
        )
    }

    OutlinedButton(
        enabled = addExerciseButtonIsEnabled,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClickedAddExercise,
    ) {
        Text(
            text = "Add Exercise to Workout",
        )
    }

    TableCard(
        header = "Exercises in Workout",
        modifier = Modifier
            .fillMaxWidth()
            .weight(weight = 1F),
        content = {

        },
    )
}

@Composable
private fun ColumnScope.CreateWorkoutPageNameWorkout(
    nameWorkout: String,
    onWorkoutNameChanged: (String) -> Unit,
) {
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
}

@Composable
private fun ColumnScope.CreateWorkoutPageRepetitions(
    numberOfReps: Int,
    onRepetitionsDecreased: () -> Unit,
    onRepetitionsIncreased: () -> Unit,
) {
    Text(
        text = "Choose Number of Repetitions",
        style = MaterialTheme.typography.titleLarge,
    )
}
//endregion Pages

//region Previews
@Preview
@Composable
private fun CreateWorkoutScreenContent_Preview(
    @PreviewParameter(AddWorkoutPagePreviewParameterProvider::class) page: AddWorkoutPage,
) {
    HiitAppTheme {
        CreateWorkoutScreenContent(
            addExerciseButtonIsEnabled = false,
            bottomBarButtonIsEnabled = true,
            nameExercise = "",
            nameWorkout = "",
            repetitions = 1,
            page = page,
            secondsDuration = 0,
            secondsRest = 0,
            onExerciseNameChanged = {},
            onDurationChanged = {},
            onRepetitionsDecreased = {},
            onRepetitionsIncreased = {},
            onRestChanged = {},
            onClickedBottomBarButton = {},
            onClickedNavigateUp = {},
            onClickedAddExercise = {},
            onWorkoutNameChanged = {},
        )
    }
}

class AddWorkoutPagePreviewParameterProvider : PreviewParameterProvider<AddWorkoutPage> {
    override val values: Sequence<AddWorkoutPage> = AddWorkoutPage.entries.toList().asSequence()
}
//endregion Previews