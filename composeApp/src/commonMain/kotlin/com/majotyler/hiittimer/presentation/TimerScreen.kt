package com.majotyler.hiittimer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.majotyler.hiittimer.presentation.common.RowClickable
import org.jetbrains.compose.ui.tooling.preview.Preview


@Preview(showBackground = true)
@Composable
fun TimerScreenPreview() {
    val viewModel = TimerViewModel()

    TimerScreen(viewModel = viewModel)
}


@Composable
fun TimerScreen(viewModel: TimerViewModel) {
    val loading by viewModel.loading.collectAsStateWithLifecycle()
    val list by viewModel.exercises.collectAsStateWithLifecycle()
    val reps by viewModel.reps.collectAsStateWithLifecycle()

    if (loading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize().background(color = Color(0xFFFFFFFF)).padding(16.dp)
        ) {
            Column(Modifier.align(Alignment.TopStart).fillMaxWidth().padding(top = 32.dp)) {
                Row() {
                    Workouts(list) { index -> viewModel.onClickedDelete(index) }
                    Spacer(Modifier.weight(1f))
                    RepsButton(
                            reps,
                        { viewModel.onEvent(TimerViewEvent.AddReps) },
                         { viewModel.onEvent(TimerViewEvent.RemoveReps) }
                    )

                }
                AddButton {
                    viewModel.onEvent(TimerViewEvent.ClickedAdd)
                }
                Spacer(Modifier.height(90.dp))
                IntervalsList()
                Spacer(Modifier.height(22.dp))
                StartButton()

            }

        }
    }


}

@Composable
fun StartButton() {
    Button(modifier=Modifier.fillMaxWidth().height(50.dp), onClick = {}){
        Text("Go", fontSize = 30.sp)
    }
}

@Composable
fun IntervalsList() {
    val list= listOf("a","ejercicio","otro")
    LazyColumn (Modifier.fillMaxWidth().height(260.dp)){
        itemsIndexed(list){
            index,item ->
            RowClickable(
                entryNo = index + 1,
                text = item,
                onClickedRow = null,
                onClickedRemove = {}
            )
        }
    }
}

@Composable
fun RepsButton(reps: Int,addReps: () -> Unit,removeReps:() ->Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center){

    Box(Modifier.height(43.dp).width(100.dp).background(MaterialTheme.colorScheme.primary),contentAlignment = Alignment.Center){
        Text("Reps", color = Color.White)
    }
        Spacer(Modifier.height(20.dp))
        Text("+", Modifier.clickable{addReps()},fontSize = 40.sp, color = MaterialTheme.colorScheme.primary )
        Text("$reps", fontSize = 40.sp,color= MaterialTheme.colorScheme.primary)
        Text("-",Modifier.clickable{removeReps()}, fontSize = 50.sp,color=MaterialTheme.colorScheme.primary)

    }

}

@Composable
fun AddButton(clickAdd: () -> Unit) {
    Box(
        Modifier.height(50.dp).width(200.dp)
            .clip(RoundedCornerShape(size = 12.dp))
            .background(MaterialTheme.colorScheme.primary)
            .clickable { clickAdd() },
        contentAlignment = Alignment.Center
    )
    {
        Text("Add+", fontSize = 22.sp, color = Color.White)

    }
}

@Composable
fun Workouts(list: List<String>, onDelete: (Int) -> Unit) {

    LazyColumn(modifier = Modifier.height(300.dp).width(200.dp)) {
        stickyHeader {
            Text(
                text = "Workouts", color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(16.dp),
            )
        }
        itemsIndexed(list) {index, item ->
            RowClickable(
                entryNo = index + 1,
                text = item,
                onClickedRow = null,
                onClickedRemove = { onDelete(index) }
            )
        }
    }
}



