package com.majotyler.hiittimer.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
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
fun RowClickable(
    entryNo: Int,
    text: String,
    onClickedRemove: () -> Unit,
    onClickedRow: (() -> Unit)? = null,
    showDivider: Boolean = true,
) {
    Column {
        Row(
            horizontalArrangement = Arrangement.spacedBy(space = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colorScheme.background)
                .run {
                    if (onClickedRow != null) {
                        clickable(
                            onClick = onClickedRow,
                            indication = ripple(),
                            interactionSource = remember { MutableInteractionSource() }
                        )
                    } else {
                        this
                    }
                }
                .padding(start = 16.dp, end = 0.dp)
                .padding(vertical = 4.dp)
                .defaultMinSize(
                    minHeight = 64.dp,
                ),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .clip(shape = RoundedCornerShape(percent = 50))
                    .padding(all = 4.dp)
            ) {
                Text(
                    color = MaterialTheme.colorScheme.secondary,
                    text = "$entryNo",
                    style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Black,
                )
            }

            Text(
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .weight(weight = 1F),
                text = text,
                style = MaterialTheme.typography.bodyLarge,
            )

            IconButton(
                onClick = onClickedRemove,
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Remove",
                )
            }
        }

        if (showDivider) {
            HorizontalDivider(
                modifier = Modifier
                    .padding(start = 16.dp),
            )
        }
    }
}

@Preview
@Composable
private fun RowClickable_Preview() {
    MaterialTheme {
        RowClickable(
            entryNo = 1,
            text = "Example text",
            onClickedRow = {},
            onClickedRemove = {},
        )
    }
}