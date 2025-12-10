package com.majotyler.hiittimer.presentation.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CardElevation
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.SubcomposeLayout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * Renders a variant of [Card] in which [content] is rendered in a card with a header.
 *
 * The width of the header section will either match the width of the [content], or, if the
 * header section is wider than the [content], will be as wide as needed to render the header.
 *
 * When the [content] section has a width less than that of the header section, the [content] is
 * horizontally-centered within the card.
 */
@Composable
fun TableCard(
    header: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    val header: @Composable (modifier: Modifier) -> Unit = {
        Text(
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            fontWeight = FontWeight.Black,
            modifier = it
                .background(color = MaterialTheme.colorScheme.primaryContainer)
                .padding(all = 16.dp),
            textAlign = TextAlign.Left,
            text = header,
            style = MaterialTheme.typography.titleLarge,
        )
    }

    val density = LocalDensity.current

    Card(
        elevation = CardDefaults.elevatedCardElevation(),
        modifier = modifier,
    ) {
        SubcomposeLayout { constraints ->
            val headerMeasurables = subcompose("header", { header(Modifier) })
            val headerPlaceables = headerMeasurables.map { it.measure(constraints) }
            val contentMeasurables = subcompose("content", content)
            val contentPlaceables = contentMeasurables.map { it.measure(constraints) }

            val contentHeight = contentPlaceables.sumOf { it.height }
            val contentWidth = contentPlaceables.sumOf { it.width }

            val headerHeight = headerPlaceables.sumOf { it.height }
            val headerWidth = headerPlaceables.sumOf { it.width }

            val width = minOf(
                a = maxOf(contentWidth, headerWidth),
                b = constraints.maxWidth,
            )
            val widthDp = density.run { width.toDp() }

            val contentAdj: @Composable () -> Unit = {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .width(width = widthDp)
                        .background(color = MaterialTheme.colorScheme.background)
                ) {
                    content()
                }
            }

            val contentAdjPlaceables = subcompose("contentAdj", contentAdj)
                .map { it.measure(constraints) }



            val headerAdj: @Composable () -> Unit = {
                header(Modifier.width(width = widthDp))
            }

            val headerAdjPlaceables = subcompose("headerAdj", headerAdj)
                .map { it.measure(constraints) }

            layout(
                width = width,
                height = minOf(
                    a = contentHeight + headerHeight,
                    b = constraints.maxHeight,
                ),
            ) {
                headerAdjPlaceables.forEach {
                    it.place(x = 0, y = 0)
                }
                contentAdjPlaceables.forEach {
                    it.place(x = 0, y = headerHeight)
                }
            }
        }
    }
}

@Preview
@Composable
private fun TableCard_Preview() {
    MaterialTheme {
        TableCard(
            header = "Workouts",
            content = {
                LazyColumn {
                    items(20) {
                        RowClickable(
                            entryNo = it,
                            text = it.toString(),
                            onClickedRow = {},
                            onClickedRemove = {},
                        )
                    }
                }
            },
        )
    }
}

@Preview
@Composable
private fun TableCard_Row_Preview() {
    MaterialTheme {
        Row(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            TableCard(
                header = "Workouts",
                modifier = Modifier.weight(weight = 1F),
                content = {
                    LazyColumn {
                        items(20) {
                            RowClickable(
                                entryNo = it,
                                text = it.toString(),
                                onClickedRow = {},
                                onClickedRemove = {},
                            )
                        }
                    }
                },
            )
            TableCard(
                header = "Test",
                content = {
                    Box(
                        modifier = Modifier
                            .width(width = 124.dp)
                            .height(height = 254.dp),
                    ) {
                        Text("test")
                    }
                }
            )
        }
    }
}


@Preview
@Composable
private fun TableCard_Row_Slim_Content_Preview() {
    MaterialTheme {
        Row(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            TableCard(
                header = "Workouts",
                modifier = Modifier.weight(weight = 1F),
                content = {
                    LazyColumn {
                        items(20) {
                            RowClickable(
                                entryNo = it,
                                text = it.toString(),
                                onClickedRow = {},
                                onClickedRemove = {},
                            )
                        }
                    }
                },
            )
            TableCard(
                header = "Test",
                content = {
                    Text("0")
                },
            )
        }
    }
}