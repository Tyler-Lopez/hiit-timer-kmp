package com.majotyler.hiittimer.presentation

import androidx.compose.runtime.Immutable

@Immutable
sealed interface TimerViewEvent {
    /** The user has clicked a button to start the timer. */
    data object ClickedAdd : TimerViewEvent
    data class ClickedDelete(val index: Int): TimerViewEvent

    data object AddReps: TimerViewEvent

    data object RemoveReps: TimerViewEvent

    /** The user has clicked a button to stop the timer. */
    //data object ClickedStopTimer : TimerViewEvent


}