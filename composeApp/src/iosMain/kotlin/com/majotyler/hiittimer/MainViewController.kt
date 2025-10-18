package com.majotyler.hiittimer

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.majotyler.hiittimer.presentation.playWorkoutScreen.PlayWorkoutScreen
import com.majotyler.hiittimer.presentation.playWorkoutScreen.PlayWorkoutVIewModel

fun MainViewController() = ComposeUIViewController {
    val vm = remember { PlayWorkoutVIewModel() }
    PlayWorkoutScreen(viewModel = vm)
}