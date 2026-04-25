package com.majotyler.hiittimer.presentation.playWorkoutScreen

import androidx.compose.runtime.Immutable

@Immutable
sealed interface PlayWorkoutViewEvent {
    /** The user has clicked the cancel button on the confirmation dialog. */
    data object ClickedDialogCancel : PlayWorkoutViewEvent

    /** The user has clicked the confirm button on the confirmation dialog. */
    data object ClickedDialogConfirm : PlayWorkoutViewEvent

    /** The user has clicked a button to start the timer. */
    data object ClickedPlay : PlayWorkoutViewEvent

    /** The user has clicked a button to stop the timer. */
    data object ClickedPause : PlayWorkoutViewEvent

    /** The user clicked the system back gesture. */
    data object ClickedSystemBack : PlayWorkoutViewEvent

    /** The user clicked the "See Workout" button to review the completed workout. */
    data object ClickedSeeWorkout : PlayWorkoutViewEvent
}