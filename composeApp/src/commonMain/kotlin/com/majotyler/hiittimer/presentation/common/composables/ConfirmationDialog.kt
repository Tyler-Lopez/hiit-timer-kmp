package com.majotyler.hiittimer.presentation.common.composables

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import com.majotyler.hiittimer.presentation.common.ui.HiitAppTheme
import hiittimer.composeapp.generated.resources.Res
import hiittimer.composeapp.generated.resources.confirmation_dialog_button_label_cancel
import hiittimer.composeapp.generated.resources.confirmation_dialog_button_label_confirm
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun ConfirmationDialog(
    text: String,
    onClickPositive: () -> Unit,
    onClickNegative: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(
                onClick = onClickPositive,
            ) {
                Text(
                    text = stringResource(resource = Res.string.confirmation_dialog_button_label_confirm),
                )
            }
        },
        dismissButton = {
            TextButton(
                onClick = onClickNegative,
            ) {
                Text(
                    text = stringResource(resource = Res.string.confirmation_dialog_button_label_cancel),
                )
            }
        },
        text = {
            Text(
                text = text,
            )
        }
    )
}

@Preview
@Composable
private fun ConfirmationDialog_Preview() {
    HiitAppTheme {
        ConfirmationDialog(
            text = "Are you sure?",
            onClickPositive = {},
            onClickNegative = {},
            onDismiss = {},
        )
    }
}

@Preview
@Composable
private fun ConfirmationDialog_Preview_DarkMode() {
    HiitAppTheme(darkTheme = true) {
        ConfirmationDialog(
            text = "Are you sure?",
            onClickPositive = {},
            onClickNegative = {},
            onDismiss = {},
        )
    }
}