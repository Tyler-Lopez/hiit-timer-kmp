package com.majotyler.hiittimer

import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.majotyler.hiittimer.presentation.TimerScreen
import com.majotyler.hiittimer.presentation.TimerViewModel

fun MainViewController() = ComposeUIViewController {
    val vm = remember { TimerViewModel() }
    TimerScreen(viewModel = vm)
}