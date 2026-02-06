package com.majotyler.hiittimer.presentation.playWorkoutScreen

/**
 * An encapsulation of the UI state required to render a timer which may be incremented.
 *
 * @param progressDisplay Display text representing the [progress] value.
 * @param progress A ratio between 0F and 1F representing the progress of the current step of the
 * workout.
 */
data class PlayWorkoutViewState(
    val confirmationDialogVisible: Boolean,
    val progressDisplay: String,
    val progress: Float,
)