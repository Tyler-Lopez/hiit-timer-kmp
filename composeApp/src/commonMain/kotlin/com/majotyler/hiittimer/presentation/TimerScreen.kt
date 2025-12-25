package com.majotyler.hiittimer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.majotyler.hiittimer.presentation.common.RowClickable
import com.majotyler.hiittimer.presentation.common.TableCard
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    val viewModel = TimerViewModel(
        router = {},
    )

    TimerScreen(viewModel = viewModel)
}


@Composable
fun TimerScreen(viewModel: TimerViewModel) {
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val list by viewModel.exercises.collectAsStateWithLifecycle()
    val reps by viewModel.reps.collectAsStateWithLifecycle()
    val intervals by viewModel.intervals.collectAsStateWithLifecycle()
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()

    if (loading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
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
                    list = list,
                    modifier = Modifier
                        .weight(weight = 1F)
                        .fillMaxHeight(),
                    onClickedAdd = { viewModel.onEvent(event = TimerViewEvent.ClickedAdd) },
                    onDelete = {
                        viewModel.onEvent(event = TimerViewEvent.ClickedDelete(index = it))
                    },
                )

                RepsButton(
                    reps = reps,
                    addReps = { viewModel.onEvent(TimerViewEvent.AddReps) },
                    removeReps = { viewModel.onEvent(TimerViewEvent.RemoveReps) },
                )

            }
            AddButton(
                clickAdd = {
                    viewModel.onEvent(
                        TimerViewEvent.AddIntervals
                    )
                },
                text = "Add Interval",
                modifier = Modifier.fillMaxWidth(),
                enabled = enabled,
            )
            IntervalsList(
                intervals = intervals,
                modifier = Modifier.weight(weight = 1F),
            ) { viewModel.onEvent(TimerViewEvent.DeleteIntervals(index = it)) }
            StartButton()
        }
    }
}

@Composable
fun StartButton() {
    Button(
        shape = RoundedCornerShape(size = 12.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = {},
    ) {
        Text(
            text = "Go",
            fontSize = 30.sp,
        )
    }
}

@Composable
fun IntervalsList(
    intervals: List<Interval>,
    modifier: Modifier = Modifier,
    deleteInterval: (Int) -> Unit,
) {
    TableCard(
        header = "Intervals",
        modifier = modifier,
        content = {
            LazyColumn(Modifier.fillMaxWidth()) {
                itemsIndexed(intervals) { index, item ->
                    RowClickable(
                        entryNo = index + 1,
                        text = "${item.reps} Repeticiones",
                        lines = item.exercises,
                        onClickedRemove = { deleteInterval(index) }
                    )
                }
            }
        }
    )
}

@Composable
fun RepsButton(
    reps: Int,
    addReps: () -> Unit,
    removeReps: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        TableCard(
            header = "Reps",
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                ) {
                    IconButton(
                        onClick = addReps,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add rep",
                        )
                    }

                    Text(
                        color = MaterialTheme.colorScheme.secondary,
                        text = "$reps",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Black,
                    )

                    IconButton(
                        onClick = removeReps,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Remove,
                            contentDescription = "Remove rep",
                        )
                    }

                }
            }
        )
    }
}


@Composable
private fun AddButton(
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    clickAdd: () -> Unit,
) {
    Button(
        shape = RoundedCornerShape(size = 12.dp),
        modifier = modifier,
        onClick = clickAdd,
        enabled = enabled
    ) {
        Text(
            fontWeight = FontWeight.SemiBold,
            text = text,
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
fun Workouts(
    list: List<String>,
    modifier: Modifier = Modifier,
    onClickedAdd: () -> Unit,
    onDelete: (index: Int) -> Unit,
) {
    Column(
        modifier = modifier,
    ) {
        TableCard(
            header = "Workouts",
            content = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(weight = 1F),
                    ) {
                        itemsIndexed(list) { index, item ->
                            RowClickable(
                                entryNo = index + 1,
                                text = item,
                                onClickedRow = null,
                                onClickedRemove = { onDelete(index) },
                                showDivider = index != list.lastIndex,
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
                            text = "Add Workout",
                            fontWeight = FontWeight.SemiBold,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            },
        )
    }


}



