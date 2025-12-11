package com.majotyler.hiittimer.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun RowWithContent(
    entryNo: Int,
    header: String,
    lines: List<String>,
    onClickedRemove: () -> Unit,
    onClickedRow: (() -> Unit)? = null,
    showDivider: Boolean = true,
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.background)
                .run {
                    if (onClickedRow != null) {
                        clickable(
                            onClick = onClickedRow,
                            indication = ripple(),
                            interactionSource = remember { MutableInteractionSource() }
                        )
                    } else this
                }
                .padding(start = 16.dp)
                .padding(vertical = 4.dp)
                .defaultMinSize(minHeight = 64.dp),
            verticalAlignment = Alignment.Top
        ) {


            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(percent = 50))
                    .padding(4.dp)
            ) {
                Text(
                    color = MaterialTheme.colorScheme.secondary,
                    text = "$entryNo",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                )
            }


            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    text = header,
                    fontWeight = FontWeight.SemiBold,
                    style = MaterialTheme.typography.bodyLarge
                )

                lines.take(3).forEach {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            IconButton(onClick = onClickedRemove) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove"
                )
            }
        }

        if (showDivider)
            HorizontalDivider(modifier = Modifier.padding(start = 16.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun RowWithContent_Preview() {
    MaterialTheme {

        RowWithContent(
            entryNo = 1,
            header = "3 reps ",
            lines = listOf("Lagartijas", "Saltos", "Burpees"),
            onClickedRemove = {},
            onClickedRow = {},
            showDivider = true
        )
    }
}

