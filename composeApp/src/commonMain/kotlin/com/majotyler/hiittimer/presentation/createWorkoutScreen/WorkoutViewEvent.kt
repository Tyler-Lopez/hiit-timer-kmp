package com.majotyler.hiittimer.presentation.createWorkoutScreen

sealed interface WorkoutViewEvent {
    data class NameWorkout(val newNameWorkout: String) : WorkoutViewEvent
    data object AddWorkout : WorkoutViewEvent
    data object ClickedAdvanceButton : WorkoutViewEvent
    data object ClickedNavigateUp : WorkoutViewEvent
}