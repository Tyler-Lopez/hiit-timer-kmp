package com.majotyler.hiittimer.presentation.createWorkoutScreen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.majotyler.hiittimer.presentation.common.RowClickable
import com.majotyler.hiittimer.presentation.common.TableCard
import org.jetbrains.compose.ui.tooling.preview.Preview

@Preview(showBackground = true)
@Composable
fun CreateWorkoutScreen(viewModel: WorkoutViewModel) {
    val nameWorkout by viewModel.nameWorkout.collectAsStateWithLifecycle()
    val enabled by viewModel.enabled.collectAsStateWithLifecycle()
    Column(
        modifier = Modifier
            .fillMaxSize().background(color = MaterialTheme.colorScheme.background)
            .safeDrawingPadding()
            .padding(horizontal = 8.dp),
        verticalArrangement = Arrangement.spacedBy(space = 8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f).padding(bottom = 6.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            WorkoutName(nameWorkout) { viewModel.onEvent(WorkoutViewEvent.NameWorkout(it)) }
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(space = 8.dp)
            ) {
                Column(Modifier.weight(1F), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    ExerciseName()
                    TimeAndRest(Modifier.weight(1f).fillMaxHeight())
                }
                AddExercise()
            }
        }
        Column(
            modifier = Modifier.weight(1f).padding(top = 4.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ExerciseList(Modifier.weight(1f).fillMaxWidth())
            ButtonReady(enabled) { viewModel.onEvent(WorkoutViewEvent.AddWorkout) }
        }
    }
}

@Composable
fun ButtonReady(enabled: Boolean, addWorkout: () -> Unit) {
    Button(
        shape = RoundedCornerShape(size = 12.dp),
        onClick = { addWorkout() },
        modifier = Modifier.fillMaxWidth(),
        enabled = enabled
    ) {
        Text(
            fontWeight = FontWeight.SemiBold,
            text = "Ready",
            style = MaterialTheme.typography.titleLarge,
        )
    }
}

@Composable
fun ExerciseList(modifier: Modifier) {
    val prub = listOf("burpees", "sentadillas", "abdominales", "saltos", "4")

    LazyColumn(modifier) {
        itemsIndexed(prub) { index, item ->
            RowClickable(
                entryNo = index + 1,
                text = "${item} ",
                onClickedRemove = {}
            )
        }
    }


}

@Composable
fun AddExercise() {
    Button(
        onClick = {},
        modifier = Modifier.fillMaxHeight(),
        shape = RoundedCornerShape(12.dp),

        ) {
        Icon(
            imageVector = Icons.Default.ArrowDownward,
            contentDescription = "Add exercise",
            tint = MaterialTheme.colorScheme.onPrimary
        )
    }
}

@Composable
fun TimeAndRest(modifier: Modifier) {
    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        TableCard(
            header = "Time",
            modifier = modifier,
            headerAlignment = TextAlign.Center,
            content = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = { }
                    ) {
                        Text(
                            text = "00.00",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            })

        TableCard(
            header = "Rest",
            modifier = modifier,
            headerAlignment = TextAlign.Center,
            content = {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    TextButton(
                        onClick = { }
                    ) {
                        Text(
                            text = "00.00",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        )


    }
}

@Composable
fun ExerciseName() {
    TableCard(header = "Exercise Name", content = {
        TextField(
            value = "",
            onValueChange = {},
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            placeholder = {
                Text("Enter your exercise")
            },
            colors = TextFieldDefaults.colors(
                focusedContainerColor = Color.Transparent,
                unfocusedContainerColor = Color.Transparent,
                disabledContainerColor = Color.Transparent,
                errorContainerColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            ),
        )

    })

}

@Composable
fun WorkoutName(nameWorkout: String, workoutName: (String) -> Unit) {
    Column() {
        TableCard(
            header = "Workout Name",
            headerAlignment = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            content = {

                TextField(
                    value = nameWorkout,
                    onValueChange = { workoutName(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp),
                    placeholder = {
                        Text("Enter your workout name")
                    },
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        disabledContainerColor = Color.Transparent,
                        errorContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent
                    ),
                )

            }

        )
    }

}