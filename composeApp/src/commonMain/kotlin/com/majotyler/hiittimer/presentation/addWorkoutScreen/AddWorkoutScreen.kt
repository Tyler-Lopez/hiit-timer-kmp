package com.majotyler.hiittimer.presentation.addWorkoutScreen

import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.majotyler.hiittimer.domain.model.Interval
import com.majotyler.hiittimer.presentation.common.RowClickable
import com.majotyler.hiittimer.presentation.common.TableCard
import com.majotyler.hiittimer.presentation.common.ui.HiitAppTheme
import hiittimer.composeapp.generated.resources.Res
import hiittimer.composeapp.generated.resources.add_workout_add_interval_button_label_add_interval
import hiittimer.composeapp.generated.resources.add_workout_add_interval_button_label_advance
import hiittimer.composeapp.generated.resources.add_workout_add_interval_duration_label
import hiittimer.composeapp.generated.resources.add_workout_add_interval_interval_name_label
import hiittimer.composeapp.generated.resources.add_workout_add_interval_rest_label
import hiittimer.composeapp.generated.resources.add_workout_add_interval_title
import hiittimer.composeapp.generated.resources.add_workout_add_interval_workout_preview_title
import hiittimer.composeapp.generated.resources.add_workout_create_reps_button_label_advance
import hiittimer.composeapp.generated.resources.add_workout_create_reps_content_description_decrease_repetitions
import hiittimer.composeapp.generated.resources.add_workout_create_reps_content_description_increase_repetitions
import hiittimer.composeapp.generated.resources.add_workout_create_reps_title
import hiittimer.composeapp.generated.resources.add_workout_name_workout_button_label_advance
import hiittimer.composeapp.generated.resources.add_workout_name_workout_label
import hiittimer.composeapp.generated.resources.add_workout_name_workout_title
import hiittimer.composeapp.generated.resources.add_workout_toolbar_title
import hiittimer.composeapp.generated.resources.content_description_navigate_up
import hiittimer.composeapp.generated.resources.interval_duration_label
import hiittimer.composeapp.generated.resources.interval_rest_label
import hiittimer.composeapp.generated.resources.seconds_long
import hiittimer.composeapp.generated.resources.seconds_short_label
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.jetbrains.compose.ui.tooling.preview.PreviewParameter
import org.jetbrains.compose.ui.tooling.preview.PreviewParameterProvider

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun CreateWorkoutScreen(
    viewModel: AddWorkoutViewModel,
) {
    val enabledAddExercise = viewModel.enabledAddInterval.collectAsStateWithLifecycle()
    val enabledAdvance = viewModel.enabledAdvance.collectAsStateWithLifecycle()
    val enabledWorkoutDecreaseRepetitions =
        viewModel.enabledDecreaseRepetitions.collectAsStateWithLifecycle()

    val exerciseName = viewModel.nameInterval.collectAsStateWithLifecycle()
    val exerciseWorkout = viewModel.nameWorkout.collectAsStateWithLifecycle()

    val intervals = viewModel.intervals.collectAsStateWithLifecycle()

    val page = viewModel.page.collectAsStateWithLifecycle()

    val secondsDuration = viewModel.secondsDuration.collectAsStateWithLifecycle()
    val secondsRest = viewModel.secondsRest.collectAsStateWithLifecycle()

    val workoutRepetitions = viewModel.workoutRepetitions.collectAsStateWithLifecycle()

    CreateWorkoutScreenContent(
        addIntervalButtonIsEnabled = enabledAddExercise.value,
        bottomBarButtonIsEnabled = enabledAdvance.value,
        enabledWorkoutDecreaseRepetitions = enabledWorkoutDecreaseRepetitions.value,
        intervals = intervals.value,
        nameInterval = exerciseName.value,
        nameWorkout = exerciseWorkout.value,
        workoutRepetitions = workoutRepetitions.value,
        page = page.value,
        secondsDuration = secondsDuration.value,
        secondsRest = secondsRest.value,
        onIntervalNameChanged = {
            viewModel.onEvent(event = AddWorkoutViewEvent.ChangedIntervalName(name = it))
        },
        onWorkoutNameChanged = {
            viewModel.onEvent(event = AddWorkoutViewEvent.ChangedWorkoutName(name = it))
        },
        onIntervalDurationChanged = {
            viewModel.onEvent(event = AddWorkoutViewEvent.ChangedIntervalDuration(seconds = it))
        },
        onIntervalRestChanged = {
            viewModel.onEvent(event = AddWorkoutViewEvent.ChangedIntervalRest(seconds = it))
        },
        onRepetitionsDecreased = {
            viewModel.onEvent(event = AddWorkoutViewEvent.WorkoutRepetitionsDecreased)
        },
        onRepetitionsIncreased = {
            viewModel.onEvent(event = AddWorkoutViewEvent.WorkoutRepetitionsIncreased)
        },
        onClickedAddInterval = {
            viewModel.onEvent(event = AddWorkoutViewEvent.ClickedAddIntervalToWorkout)
        },
        onClickedBottomBarButton = {
            viewModel.onEvent(event = AddWorkoutViewEvent.ClickedAdvance)
        },
        onClickedNavigateUp = {
            viewModel.onEvent(event = AddWorkoutViewEvent.ClickedNavigateUp)
        },
        onClickedRemoveInterval = {
            viewModel.onEvent(event = AddWorkoutViewEvent.ClickedDeleteInterval(index = it))
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CreateWorkoutScreenContent(
    addIntervalButtonIsEnabled: Boolean,
    bottomBarButtonIsEnabled: Boolean,
    enabledWorkoutDecreaseRepetitions: Boolean,
    intervals: List<Interval>,
    nameInterval: String,
    nameWorkout: String,
    workoutRepetitions: Int,
    page: AddWorkoutPage,
    secondsDuration: Int,
    secondsRest: Int,
    onClickedAddInterval: () -> Unit,
    onClickedRemoveInterval: (Int) -> Unit,
    onIntervalNameChanged: (String) -> Unit,
    onWorkoutNameChanged: (String) -> Unit,
    onIntervalDurationChanged: (String) -> Unit,
    onIntervalRestChanged: (String) -> Unit,
    onRepetitionsDecreased: () -> Unit,
    onRepetitionsIncreased: () -> Unit,
    onClickedBottomBarButton: () -> Unit,
    onClickedNavigateUp: () -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(resource = Res.string.add_workout_toolbar_title))
                },
                navigationIcon = {
                    IconButton(
                        onClick = onClickedNavigateUp,
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(
                                resource = Res.string.content_description_navigate_up,
                            ),
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
                                text = stringResource(
                                    resource = when (page) {
                                        AddWorkoutPage.ADD_INTERVAL -> Res
                                            .string
                                            .add_workout_add_interval_button_label_advance

                                        AddWorkoutPage.NAME_WORKOUT -> Res
                                            .string
                                            .add_workout_name_workout_button_label_advance

                                        AddWorkoutPage.SELECT_REPS -> Res
                                            .string
                                            .add_workout_create_reps_button_label_advance
                                    }
                                ),
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
                        AddWorkoutPage.ADD_INTERVAL -> CreateWorkoutPageAddIntervals(
                            addIntervalButtonIsEnabled = addIntervalButtonIsEnabled,
                            intervals = intervals,
                            nameInterval = nameInterval,
                            secondsDuration = secondsDuration,
                            secondsRest = secondsRest,
                            onClickedAddInterval = onClickedAddInterval,
                            onClickedRemoveInterval = onClickedRemoveInterval,
                            onIntervalDurationChanged = onIntervalDurationChanged,
                            onIntervalRestChanged = onIntervalRestChanged,
                            onIntervalNameChanged = onIntervalNameChanged,
                        )

                        AddWorkoutPage.NAME_WORKOUT -> CreateWorkoutPageNameWorkout(
                            nameWorkout = nameWorkout,
                            onWorkoutNameChanged = onWorkoutNameChanged,
                        )

                        AddWorkoutPage.SELECT_REPS -> CreateWorkoutPageRepetitions(
                            enabledWorkoutDecreaseRepetitions = enabledWorkoutDecreaseRepetitions,
                            numberOfReps = workoutRepetitions,
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
private fun ColumnScope.CreateWorkoutPageAddIntervals(
    addIntervalButtonIsEnabled: Boolean,
    intervals: List<Interval>,
    nameInterval: String,
    secondsDuration: Int,
    secondsRest: Int,
    onClickedAddInterval: () -> Unit,
    onClickedRemoveInterval: (Int) -> Unit,
    onIntervalDurationChanged: (String) -> Unit,
    onIntervalRestChanged: (String) -> Unit,
    onIntervalNameChanged: (String) -> Unit,
) {
    Text(
        text = stringResource(resource = Res.string.add_workout_add_interval_title),
        style = MaterialTheme.typography.titleLarge,
    )

    TextField(
        value = nameInterval,
        maxLines = 1,
        onValueChange = onIntervalNameChanged,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                text = stringResource(
                    resource = Res.string.add_workout_add_interval_interval_name_label,
                ),
            )
        },
    )

    Row(
        horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
    ) {
        TextField(
            maxLines = 1,
            label = {
                Text(
                    text = stringResource(
                        resource = Res.string.add_workout_add_interval_duration_label,
                    ),
                )
            },
            value = secondsDuration.toString(),
            onValueChange = { onIntervalDurationChanged(it) },
            modifier = Modifier.weight(weight = 1F),
            suffix = {
                Text(
                    text = stringResource(
                        resource = Res.string.seconds_short_label,
                    ),
                )
            },
        )

        TextField(
            maxLines = 1,
            label = {
                Text(
                    text = stringResource(
                        resource = Res.string.add_workout_add_interval_rest_label,
                    ),
                )
            },
            value = secondsRest.toString(),
            onValueChange = { onIntervalRestChanged(it) },
            modifier = Modifier.weight(weight = 1F),
            suffix = {
                Text(
                    text = stringResource(
                        resource = Res.string.seconds_short_label,
                    ),
                )
            },
        )
    }

    OutlinedButton(
        enabled = addIntervalButtonIsEnabled,
        modifier = Modifier.fillMaxWidth(),
        onClick = onClickedAddInterval,
    ) {
        Text(
            text = stringResource(
                resource = Res.string.add_workout_add_interval_button_label_add_interval,
            ),
        )
    }

    TableCard(
        header = stringResource(
            resource = Res.string.add_workout_add_interval_workout_preview_title,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .weight(weight = 1F),
        content = {
            LazyColumn {
                itemsIndexed(intervals) { index, interval ->
                    val durationSeconds = pluralStringResource(
                        resource = Res.plurals.seconds_long,
                        quantity = interval.duration,
                        formatArgs = arrayOf(interval.duration),
                    )

                    val restSeconds = pluralStringResource(
                        resource = Res.plurals.seconds_long,
                        quantity = interval.rest,
                        formatArgs = arrayOf(interval.rest),
                    )

                    RowClickable(
                        entryNo = index + 1,
                        text = interval.name,
                        onClickedRemove = {
                            onClickedRemoveInterval(index)
                        },
                        onClickedRow = null,
                        showDivider = index != intervals.lastIndex,
                        lines = listOf(
                            stringResource(
                                resource = Res.string.interval_duration_label,
                                formatArgs = arrayOf(durationSeconds),
                            ),
                            stringResource(
                                resource = Res.string.interval_rest_label,
                                formatArgs = arrayOf(restSeconds),
                            ),
                        )
                    )
                }
            }
        },
    )
}

@Composable
private fun ColumnScope.CreateWorkoutPageNameWorkout(
    nameWorkout: String,
    onWorkoutNameChanged: (String) -> Unit,
) {
    Text(
        text = stringResource(
            resource = Res.string.add_workout_name_workout_title,
        ),
        style = MaterialTheme.typography.titleLarge,
    )

    TextField(
        value = nameWorkout,
        maxLines = 1,
        onValueChange = onWorkoutNameChanged,
        modifier = Modifier.fillMaxWidth(),
        label = {
            Text(
                text = stringResource(
                    resource = Res.string.add_workout_name_workout_label,
                ),
            )
        },
    )
}

@Composable
private fun ColumnScope.CreateWorkoutPageRepetitions(
    enabledWorkoutDecreaseRepetitions: Boolean,
    numberOfReps: Int,
    onRepetitionsDecreased: () -> Unit,
    onRepetitionsIncreased: () -> Unit,
) {
    Text(
        text = stringResource(
            resource = Res.string.add_workout_create_reps_title,
        ),
        style = MaterialTheme.typography.titleLarge,
    )

    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        IconButton(
            onClick = onRepetitionsDecreased,
            content = {
                Icon(
                    contentDescription = stringResource(
                        resource = Res
                            .string
                            .add_workout_create_reps_content_description_decrease_repetitions,
                    ),
                    imageVector = Icons.Default.Remove,
                )
            },
            enabled = enabledWorkoutDecreaseRepetitions,
        )

        Text(
            text = numberOfReps.toString(),
        )

        IconButton(
            onClick = onRepetitionsIncreased,
            content = {
                Icon(
                    contentDescription = stringResource(
                        resource = Res
                            .string
                            .add_workout_create_reps_content_description_increase_repetitions,
                    ),
                    imageVector = Icons.Default.Add,
                )
            }
        )
    }
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
            addIntervalButtonIsEnabled = false,
            bottomBarButtonIsEnabled = true,
            intervals = emptyList(),
            nameInterval = "",
            nameWorkout = "",
            workoutRepetitions = 1,
            page = page,
            secondsDuration = 0,
            secondsRest = 0,
            onIntervalNameChanged = {},
            onIntervalDurationChanged = {},
            onIntervalRestChanged = {},
            onRepetitionsDecreased = {},
            onRepetitionsIncreased = {},
            onClickedBottomBarButton = {},
            onClickedRemoveInterval = {},
            onClickedNavigateUp = {},
            onClickedAddInterval = {},
            enabledWorkoutDecreaseRepetitions = false,
            onWorkoutNameChanged = {},
        )
    }
}

class AddWorkoutPagePreviewParameterProvider : PreviewParameterProvider<AddWorkoutPage> {
    override val values: Sequence<AddWorkoutPage> = AddWorkoutPage.entries.toList().asSequence()
}
//endregion Previews