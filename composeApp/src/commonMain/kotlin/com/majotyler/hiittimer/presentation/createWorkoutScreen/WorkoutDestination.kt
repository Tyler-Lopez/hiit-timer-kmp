package com.majotyler.hiittimer.presentation.createWorkoutScreen

sealed interface WorkoutDestination {
    data object NavigateToTimer : WorkoutDestination
}