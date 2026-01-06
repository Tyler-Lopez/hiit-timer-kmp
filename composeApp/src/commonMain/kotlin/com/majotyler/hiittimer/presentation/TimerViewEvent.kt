package com.majotyler.hiittimer.presentation

import androidx.compose.runtime.Immutable

@Immutable
sealed interface TimerViewEvent {
    data object ClickedAddWorkout : TimerViewEvent
    data class ClickedDeleteWorkout(val index: Int) : TimerViewEvent
}