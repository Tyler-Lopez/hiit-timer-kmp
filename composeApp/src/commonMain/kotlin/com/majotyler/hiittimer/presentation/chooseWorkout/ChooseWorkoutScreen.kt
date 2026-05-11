package com.majotyler.hiittimer.presentation.chooseWorkout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.common.RowClickable
import com.majotyler.hiittimer.presentation.common.TableCard
import hiittimer.composeapp.generated.resources.Res
import hiittimer.composeapp.generated.resources.choose_workout_button_label_create_workout
import hiittimer.composeapp.generated.resources.choose_workout_button_label_go
import hiittimer.composeapp.generated.resources.choose_workout_card_header_workout
import org.jetbrains.compose.resources.stringResource


@Composable
fun ChooseWorkoutScreen(viewModel: ChooseWorkoutViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
    ) {
        WorkoutCard(
            workout = state.workout,
            modifier = Modifier
                .weight(weight = 1F)
                .fillMaxWidth(),
            onClickedCreate = {
                viewModel.onEvent(event = ChooseWorkoutViewEvent.ClickedCreateWorkout)
            },
            onClickedRemove = {
                viewModel.onEvent(event = ChooseWorkoutViewEvent.ClickedRemoveWorkout)
            },
        )
        Button(
            enabled = state.workout != null,
            shape = RoundedCornerShape(size = 12.dp),
            modifier = Modifier.fillMaxWidth(),
            onClick = { viewModel.onEvent(ChooseWorkoutViewEvent.ClickedGo) },
        ) {
            Text(
                text = stringResource(resource = Res.string.choose_workout_button_label_go),
                fontSize = 30.sp,
            )
        }
    }
}

@Composable
private fun WorkoutCard(
    workout: Workout?,
    modifier: Modifier = Modifier,
    onClickedCreate: () -> Unit,
    onClickedRemove: () -> Unit,
) {
    Column(modifier = modifier) {
        TableCard(
            header = stringResource(resource = Res.string.choose_workout_card_header_workout),
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    if (workout != null) {
                        RowClickable(
                            entryNo = 1,
                            text = workout.name,
                            lines = listOf(
                                workout.intervals.joinToString { it.name },
                                workout.repetitions.toString(),
                            ),
                            onClickedRow = null,
                            onClickedRemove = onClickedRemove,
                            showDivider = false,
                        )
                    }
                    HorizontalDivider()
                    TextButton(onClick = onClickedCreate) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                        )
                        Spacer(modifier = Modifier.width(width = 2.dp))
                        Text(
                            text = stringResource(resource = Res.string.choose_workout_button_label_create_workout),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            },
        )
    }
}
