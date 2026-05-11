package com.majotyler.hiittimer.presentation.createWorkoutScreen

sealed interface CreateWorkoutViewEvent {
    data class ChangedIntervalDuration(val seconds: String) : CreateWorkoutViewEvent
    data class ChangedIntervalRest(val seconds: String) : CreateWorkoutViewEvent
    data class ChangedIntervalName(val name: String) : CreateWorkoutViewEvent
    data class ChangedWorkoutName(val name: String) : CreateWorkoutViewEvent
    data object ClickedAddIntervalToWorkout : CreateWorkoutViewEvent
    data object ClickedAdvance : CreateWorkoutViewEvent
    data class ClickedDeleteInterval(val index: Int) : CreateWorkoutViewEvent
    data object ClickedNavigateUp : CreateWorkoutViewEvent
    data object WorkoutRepetitionsDecreased : CreateWorkoutViewEvent
    data object WorkoutRepetitionsIncreased : CreateWorkoutViewEvent
}
