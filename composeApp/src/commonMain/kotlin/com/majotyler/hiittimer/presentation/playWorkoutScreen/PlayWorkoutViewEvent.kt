package com.majotyler.hiittimer.presentation.playWorkoutScreen

import androidx.compose.runtime.Immutable

@Immutable
sealed interface PlayWorkoutViewEvent {
    /** The user has clicked a button to start the timer. */
    data object ClickedPlay : PlayWorkoutViewEvent

    /** The user has clicked a button to stop the timer. */
    data object ClickedPause : PlayWorkoutViewEvent
    data object Enabled : PlayWorkoutViewEvent
}