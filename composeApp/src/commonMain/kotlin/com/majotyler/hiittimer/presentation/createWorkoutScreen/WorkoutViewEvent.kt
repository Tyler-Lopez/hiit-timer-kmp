package com.majotyler.hiittimer.presentation.createWorkoutScreen

sealed interface WorkoutViewEvent {
    data class NameWorkout(val newNameWorkout: String) : WorkoutViewEvent
    data object ClickedAddExercise : WorkoutViewEvent
    data object ClickedAdvance : WorkoutViewEvent
    data object ClickedNavigateUp : WorkoutViewEvent
}