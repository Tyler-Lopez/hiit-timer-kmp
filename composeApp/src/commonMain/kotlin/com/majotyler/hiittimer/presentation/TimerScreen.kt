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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.majotyler.hiittimer.presentation.common.RowClickable
import com.majotyler.hiittimer.presentation.common.RowWithContent
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
    val enable by viewModel.enable.collectAsStateWithLifecycle()

    if (loading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize().background(color = Color(0xFFFFFFFF)).padding(16.dp)
        ) {
            Column(Modifier.align(Alignment.TopStart).fillMaxWidth().padding(top = 32.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth().fillMaxHeight(0.5f),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
                ) {
                    Workouts(
                        list = list,
                        modifier = Modifier
                            .weight(weight = 3F).fillMaxHeight(),
                        onClickedAdd = { viewModel.onEvent(event = TimerViewEvent.ClickedAdd) },
                        onDelete = {
                            viewModel.onEvent(event = TimerViewEvent.ClickedDelete(index = it))
                        },
                    )

                    RepsButton(
                        reps = reps,
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        addReps = { viewModel.onEvent(TimerViewEvent.AddReps) },
                        removeReps = { viewModel.onEvent(TimerViewEvent.RemoveReps) },
                        enable = enable,
                        addIntervals = {
                            viewModel.onEvent(
                                TimerViewEvent.AddIntervals
                            )
                        }
                    )

                }
                Spacer(Modifier.height(20.dp))
                IntervalsList(intervals) { viewModel.onEvent(TimerViewEvent.DeleteIntervals(index = it)) }
                Spacer(Modifier.height(22.dp))
                StartButton()

            }

        }
    }


}

@Composable
fun StartButton() {
    Button(modifier = Modifier.fillMaxWidth().height(50.dp), onClick = {}) {
        Text("Go", fontSize = 30.sp)
    }
}

@Composable
fun IntervalsList(intervals: List<Interval>, deleteInterval: (Int) -> Unit) {
    TableCard(header = "Intervals", content = {
        LazyColumn(Modifier.fillMaxWidth().fillMaxHeight(0.65f)) {
            itemsIndexed(intervals) { index, item ->
                RowWithContent(
                    entryNo = index + 1,
                    header = "${item.reps} Repeticiones",
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
    modifier: Modifier,
    addIntervals: () -> Unit,
    enable: Boolean
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
        Spacer(Modifier.weight(1f))
        AddButton(addIntervals, "Add", enable = enable)
    }
}


@Composable
private fun AddButton(
    clickAdd: () -> Unit, text: String, enable: Boolean
) {
    Button(
        shape = RoundedCornerShape(size = 12.dp),
        modifier = Modifier.fillMaxWidth(),
        onClick = clickAdd,
        enabled = enable
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth().fillMaxHeight(0.7f)
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
            },
        )
        Spacer(Modifier.weight(1f))
        AddButton(clickAdd = onClickedAdd, "Add Workout", true)
    }


}



