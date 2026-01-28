package com.majotyler.hiittimer.presentation.addWorkoutScreen

sealed interface AddWorkoutViewEvent {
    data class ChangedIntervalDuration(val seconds: String) : AddWorkoutViewEvent
    data class ChangedIntervalRest(val seconds: String) : AddWorkoutViewEvent
    data class ChangedIntervalName(val name: String) : AddWorkoutViewEvent
    data class ChangedWorkoutName(val name: String) : AddWorkoutViewEvent
    data object ClickedAddIntervalToWorkout : AddWorkoutViewEvent
    data object ClickedAdvance : AddWorkoutViewEvent
    data class ClickedDeleteInterval(val index: Int) : AddWorkoutViewEvent
    data object ClickedNavigateUp : AddWorkoutViewEvent
    data object WorkoutRepetitionsDecreased : AddWorkoutViewEvent
    data object WorkoutRepetitionsIncreased : AddWorkoutViewEvent
}