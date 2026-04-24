package com.majotyler.hiittimer.presentation.buildWorkouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.majotyler.hiittimer.domain.model.Interval
import com.majotyler.hiittimer.domain.model.Workout
import com.majotyler.hiittimer.presentation.common.TableCard
import com.majotyler.hiittimer.presentation.common.composables.DurationLabel
import com.majotyler.hiittimer.presentation.common.composables.RestLabel
import hiittimer.composeapp.generated.resources.Res
import hiittimer.composeapp.generated.resources.build_workouts_button_label_add_workout
import hiittimer.composeapp.generated.resources.build_workouts_button_label_go
import hiittimer.composeapp.generated.resources.build_workouts_card_header_workouts
import hiittimer.composeapp.generated.resources.build_workouts_content_description_remove_workout
import hiittimer.composeapp.generated.resources.build_workouts_workout_repetitions
import hiittimer.composeapp.generated.resources.build_workouts_workout_total_duration
import org.jetbrains.compose.resources.pluralStringResource
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview(showBackground = true)
@Composable
fun BuildWorkoutsScreen_Preview() {
    val viewModel = BuildWorkoutsViewModel(
        router = {},
    )

    BuildWorkoutsScreen(viewModel = viewModel)
}


@Composable
fun BuildWorkoutsScreen(viewModel: BuildWorkoutsViewModel) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.background)
            .fillMaxSize()
            .safeDrawingPadding()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(space = 4.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .weight(weight = 1F),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
        ) {
            Workouts(
                list = state.workouts,
                modifier = Modifier
                    .weight(weight = 1F)
                    .fillMaxHeight(),
                onClickedAdd = { viewModel.onEvent(event = BuildWorkoutsViewEvent.ClickedAddWorkout) },
                onDelete = {
                    viewModel.onEvent(event = BuildWorkoutsViewEvent.ClickedDeleteWorkout(index = it))
                },
            )
        }
        StartButton(
            enabled = state.enabledButtonPlayWorkouts,
        ) { viewModel.onEvent(BuildWorkoutsViewEvent.ClickedGo) }
    }
}

@Composable
fun StartButton(
    enabled: Boolean,
    onClickedGo: () -> Unit,
) {
    Button(
        enabled = enabled,
        shape = RoundedCornerShape(size = 12.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClickedGo() },
    ) {
        Text(
            text = stringResource(resource = Res.string.build_workouts_button_label_go),
            fontSize = 30.sp,
        )
    }
}

@Composable
fun Workouts(
    list: List<Workout>,
    modifier: Modifier = Modifier,
    onClickedAdd: () -> Unit,
    onDelete: (index: Int) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        TableCard(
            header = stringResource(resource = Res.string.build_workouts_card_header_workouts),
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth(),
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 1F),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = androidx.compose.foundation.layout.PaddingValues(8.dp),
                    ) {
                        itemsIndexed(list) { index, workout ->
                            WorkoutCard(
                                workout = workout,
                                onDelete = { onDelete(index) },
                            )
                        }
                    }
                    HorizontalDivider()
                    TextButton(
                        onClick = onClickedAdd,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = null,
                        )
                        Spacer(
                            modifier = Modifier.width(width = 2.dp),
                        )
                        Text(
                            text = stringResource(resource = Res.string.build_workouts_button_label_add_workout),
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            },
        )
    }
}

@Composable
private fun WorkoutCard(
    workout: Workout,
    onDelete: () -> Unit,
) {
    val totalSeconds = workout.intervals.sumOf { it.duration + it.rest } * workout.repetitions
    val totalDisplay = formatSeconds(totalSeconds)
    val repsLabel = pluralStringResource(
        resource = Res.plurals.build_workouts_workout_repetitions,
        quantity = workout.repetitions,
        formatArgs = arrayOf(workout.repetitions),
    )
    val totalLabel = stringResource(
        resource = Res.string.build_workouts_workout_total_duration,
        formatArgs = arrayOf(totalDisplay),
    )

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = RoundedCornerShape(12.dp),
    ) {
        Column(modifier = Modifier.padding(start = 12.dp, top = 12.dp, bottom = 12.dp, end = 0.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = workout.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "$repsLabel  ·  $totalLabel",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                    )
                }
                IconButton(onClick = onDelete) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(resource = Res.string.build_workouts_content_description_remove_workout),
                    )
                }
            }

            if (workout.intervals.isNotEmpty()) {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                )
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    workout.intervals.forEach { interval ->
                        IntervalRow(interval = interval)
                    }
                }
            }
        }
    }
}

@Composable
private fun IntervalRow(interval: Interval) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(end = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = interval.name,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            modifier = Modifier.weight(1f),
        )
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            DurationLabel(seconds = interval.duration)
            RestLabel(seconds = interval.rest)
        }
    }
}

private fun formatSeconds(totalSeconds: Int): String {
    val minutes = totalSeconds / 60
    val seconds = totalSeconds % 60
    return when {
        minutes == 0 -> "${seconds}s"
        seconds == 0 -> "${minutes}m"
        else -> "${minutes}m ${seconds}s"
    }
}
