package com.majotyler.hiittimer.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.IconButton
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

    if (loading) {
        Box(Modifier.fillMaxSize()) {
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        }
    } else {
        Box(
            modifier = Modifier.fillMaxSize().background(color = Color(0xFFFFFFFF)).padding(16.dp)
        ) {
            Column(Modifier.align(Alignment.TopStart)) {
                Workouts(list){ item -> viewModel.onDelete(item) }
                AddButton {
                    viewModel.onEvent(TimerViewEvent.ClickAdd)
                }

            }

        }
    }


}

@Composable
fun AddButton(clickAdd: () -> Unit) {
    Box(
        Modifier.height(50.dp).width(200.dp)
            .clip(RoundedCornerShape(size = 12.dp))
            .background(Color.Black)
            .clickable { clickAdd() },
        contentAlignment = Alignment.Center
    )
    {
        Text("Add+", fontSize = 22.sp, color = Color.White)

    }
}

@Composable
fun Workouts(list: List<String>, onDelete: (String) -> Unit) {

    LazyColumn(modifier = Modifier.height(300.dp).width(200.dp)) {
        stickyHeader {
            Text(
                text = "Workouts", color = Color.White,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Black)
                    .padding(16.dp),
            )
        }
        itemsIndexed(list) {index, item ->
            ItemList(index,item, onDelete)

        }
    }
}

@Composable
fun ItemList(index: Int, item: String, onDelete: (String) -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = "${index + 1}. ${item}",
                fontSize = 20.sp,
                modifier = Modifier.weight(1f)
            )

            IconButton(
                onClick = { onDelete(item) },
                modifier = Modifier
                    .size(28.dp)
                    .background(Color.Black).padding(5.dp)
            ) {
                Text(
                    text = "X",
                    color = Color.White,
                    fontSize = 14.sp
                )
            }
        }

        Divider(color = Color.LightGray, thickness = 1.dp)
    }
}



