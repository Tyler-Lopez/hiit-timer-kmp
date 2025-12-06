package com.majotyler.hiittimer.presentation

import androidx.compose.runtime.Immutable

@Immutable
sealed interface TimerViewEvent {
    /** The user has clicked a button to start the timer. */
    data object ClickAdd : TimerViewEvent
    data class onDelete(val item: String): TimerViewEvent

    /** The user has clicked a button to stop the timer. */
    //data object ClickedStopTimer : TimerViewEvent


}